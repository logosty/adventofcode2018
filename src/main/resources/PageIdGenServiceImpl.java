import com.page.idgen.dao.IdGenDao;
import com.page.idgen.model.IdGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class PageIdGenServiceImpl implements PageIdGenService {

    private static final Logger logger = LoggerFactory.getLogger(PageIdGenServiceImpl.class);

    @Autowired
    IdGenDao idGenDao;

    private static volatile long upper; //当次批量申请id的上限,等于下次批量申请id的起始值 注:本次申请的范围之内不可使用，否则会造成分配重复id

    private static volatile AtomicLong currentId = null; //当前已分配出去的id

    private static final String TABLE_NAME_PREFIX = "t_page";

    @Override
    public long nextId() {
        if (currentId == null || currentId.get() >= upper) {
            synchronized (PageIdGenServiceImpl.class) {
                if (currentId == null || currentId.get() >= upper) {
                    int maxRetry = 6;
                    while (maxRetry >= 0) {
                        IdGen idGen = idGenDao.getIdGenByTableName(TABLE_NAME_PREFIX);
                        if (idGen != null) {
                            currentId = new AtomicLong(idGen.getNextId());
                            upper = idGen.getNextId() + idGen.getIncr();
                            int row = idGenDao.updateNextId(TABLE_NAME_PREFIX, idGen.getDataVersion());
                            if (row == 1) { // 如更新db成功
                                logger.debug("allocate groupId range success: [{}, {})", currentId.get(), upper);
                                break;
                            }
                        }
                        maxRetry--;
                    }
                    if (maxRetry < 0) {
                        String errrorMessage = String.format("fail to allocate groupId range: [%d, %d)", currentId.get(), upper).toString();
                        logger.error(errrorMessage);
                        throw new RuntimeException(errrorMessage);
                    }
                } else {
                    logger.error("fail to get IdGen: tableName={} currentId={} upper={}", TABLE_NAME_PREFIX, currentId.get(), upper);
                }
            }
        }
        long nextId = currentId.getAndIncrement();
        return nextId;
    }

   
    

}
