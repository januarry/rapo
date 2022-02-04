package  com.tms.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;




@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RedisCacheRepository {

	public static final String KEY = "QUESTION";

	public static final String QUE_KEY = "QUESTIONS";

	public static final String EXAM = "EXAM";

	public static final String QUESTIONRESULT = "QUESTIONRESULT";

	public static final String NUMERICQUESTIONRESULT = "NUMERICQUESTIONRESULT";

	public static final String MATRIXQUESTIONRESULT = "MATRIXQUESTIONRESULT";

	public static final String EXAMTYPE = "EXAMTYPE";
	public static final String INSTRUCTIONCACHE = "INSTRUCTIONCACHE";

	public static final String EXAMSCHEDULEMARKS = "EXAMSCHEDULEMARKS";
	public static final String EXAMSCHEDULEMARK = "EXAMSCHEDULEMARK";

	public static final String EXAMSCACHE = "EXAMSCACHE";

	public static final String EXAMSECTIONCACHE = "EXAMSECTIONCACHE";

	public static final String EXAMSUBJECTCACHE = "EXAMSUBJECTCACHE";

	public static final String EXAMATTEMPTCACHE = "EXAMATTEMPTCACHE";

	public static final String EXAMQUESTIONCACHE = "EXAMQUESTIONCACHE";

	public static final String QUESTION = "QUESTION";

	public static final String EXAMQUESTIONATTEMPT = "EXAMQUESTIONATTEMPT";

	public static final String EXAMQUESTIONSLISTCACHE = "EXAMQUESTIONSLISTCACHE";

	public static final String TESTSCHEDULECACHE = "TESTSCHEDULECACHE";
	public static final String QUESTIONCACHE = "QUESTIONCACHE";

	public static final String STDHEARTBEATCACHE = "STDHEARTBEATCACHE";
	
	public static final String EXAMSCHEDULELISTCACHE = "EXAMSCHEDULELISTCACHE";
	
	public static final String STUDENTBIOCACHE = "STUDENTBIOCACHE";
	public static final String STUDENTPROGRAMSCACHE = "STUDENTPROGRAMSCACHE";

	private HashOperations hashOperations;

	@Qualifier("redisWriteTemplate")
	@Autowired
	private RedisTemplate redisTemplate;
	
	public RedisCacheRepository(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOperations = this.redisTemplate.opsForHash();
	}

	

}