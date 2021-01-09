package com.summer.qa.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

/**
 * @author ：lightingSummer
 * @date ：2019/5/28 0028
 * @description：
 */
@Service
public class JedisAdapter implements InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

  private JedisPool pool = null;

  @Value("${jedis.host}")
  private String host;

  @Value("${jedis.port}")
  private Integer port;

  @Value("${jedis.password}")
  private String pwd;

  @Value("${jedis.connectionTimeout}")
  private Integer timeout;

  @Override
  public void afterPropertiesSet() {
    try {
      pool = new JedisPool(new GenericObjectPoolConfig(), host, port, timeout, pwd);
    } catch (Exception e) {
      logger.error("Create JedisPool failed :" + e.getMessage());
    }
  }

  public Jedis getJedis() {
    return pool.getResource();
  }

  public String get(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.get(key);
    } catch (Exception e) {
      logger.error("Jedis get exception : " + e.getMessage());
      return null;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public void set(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      jedis.set(key, value);
    } catch (Exception e) {
      logger.error("Jedis set exception : " + e.getMessage());
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public long sadd(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.sadd(key, value);
    } catch (Exception e) {
      logger.error("Jedis sadd exception : " + e.getMessage());
      return 0;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public boolean sismember(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.sismember(key, value);
    } catch (Exception e) {
      logger.error("Jedis sismember exception : " + e.getMessage());
      return false;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public long srem(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.srem(key, value);
    } catch (Exception e) {
      logger.error("Jedis srem exception : " + e.getMessage());
      return 0;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public long scard(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.scard(key);
    } catch (Exception e) {
      logger.error("Jedis scard exception : " + e.getMessage());
      return 0;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public long lpush(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.lpush(key, value);
    } catch (Exception e) {
      logger.error("Jedis lpush exception : " + e.getMessage());
      return 0;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public List<String> brpop(int timeout, String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.brpop(timeout, key);
    } catch (Exception e) {
      logger.error("Jedis brpop exception : " + e.getMessage());
      return null;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public Transaction multi(Jedis jedis) {
    try {
      return jedis.multi();
    } catch (Exception e) {
      logger.error("发生异常" + e.getMessage());
    }
    return null;
  }

  public List<Object> exec(Transaction tx, Jedis jedis) {
    try {
      return tx.exec();
    } catch (Exception e) {
      logger.error("发生异常" + e.getMessage());
      tx.discard();
    } finally {
      if (tx != null) {
        try {
          tx.close();
        } catch (Exception e) {
          logger.error("发生异常" + e.getMessage());
        }
      }

      if (jedis != null) {
        jedis.close();
      }
    }
    return null;
  }

  public Set<String> zrevrange(String key, int start, int end) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.zrevrange(key, start, end);
    } catch (Exception e) {
      logger.error("发生异常" + e.getMessage());
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
    return null;
  }

  public long zcard(String key) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.zcard(key);
    } catch (Exception e) {
      logger.error("发生异常" + e.getMessage());
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
    return 0;
  }

  public Double zscore(String key, String member) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.zscore(key, member);
    } catch (Exception e) {
      logger.error("发生异常" + e.getMessage());
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
    return null;
  }

  public void setObject(String key, Object obj) {
    set(key, JSON.toJSONString(obj));
  }

  public <T> T getObject(String key, Class<T> clazz) {
    String obj = get(key);
    if (obj != null) {
      return JSON.parseObject(obj, clazz);
    }
    return null;
  }

  public List<String> lrange(String key, int start, int end) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.lrange(key, start, end);
    } catch (Exception e) {
      logger.error("发生异常" + e.getMessage());
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
    return null;
  }
}
