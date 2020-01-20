package com.example.redis.token.api.config.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存 操作
 *
 * @author laughing
 * @date 2018/11/6 16:44
 */
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 关闭redis连接
     *
     * @author xiaog.li
     * @date 2019/7/10 16:56
     */
    private void close() {
        RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
    }

    /**
     * 指定缓存失效时间
     *
     * @param [key, time]
     * @return boolean
     * @author laughing
     * @date 2018/11/6 16:53
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }

    }

    /**
     * 获取key的过期时间
     *
     * @param key
     * @return long
     * @author laughing
     * @date 2018/11/6 16:57
     */
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } finally {
            close();
        }

    }

    /**
     * 删除缓存
     *
     * @param [key]
     * @return void
     * @author laughing
     * @date 2018/11/6 16:59
     */
    public Long delete(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    Boolean isDelete = redisTemplate.delete(key[0]);
                    if (isDelete) {
                        return 1L;
                    }
                    return 0L;
                } else {
                    return redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
            return -1L;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        } finally {
            close();
        }

    }

    /**
     * 取值
     *
     * @param key 关键字
     * @return java.lang.Object
     * @author laughing
     * @date 2018/11/6 17:08
     */
    public Object getValue(String key) {
        try {
            if (key == null || "".equals(key)) {
                return null;
            }
            if (redisTemplate.hasKey(key)) {
                return redisTemplate.opsForValue().get(key);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }

    }

    /**
     * 判断key存在
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        try {
            if (key == null || "".equals(key)) {
                return false;
            }
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }

    }


    /**
     * 获取对象数据
     *
     * @author xiaog.li
     * @date 2019/7/10 16:57
     */
    public <T> T get(String key, Class<T> tClass) {
        try {
            if (key == null || "".equals(key)) {
                return null;
            }
            if (redisTemplate.hasKey(key)) {
                Object value = redisTemplate.opsForValue().get(key);
                if (value == null || "".equals(value)) {
                    return null;
                }
                return JSON.parseObject(String.valueOf(value), tClass);
            }
            return null;
        } finally {
            close();
        }
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value) {
        try {
            if (key == null || "".equals(key) || value == null || "".equals(value)) {
                return false;
            }
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }

    }

    /*
     *设置缓存
     * @author laughing
     * @date 2018/11/7 11:20
     * @param [key, value, time]
     * @return boolean
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (key == null || "".equals(key) || value == null || "".equals(value)) {
                return false;
            }
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                return true;
            } else {
                set(key, value);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }

    }

    /**
     * 设置缓存
     *
     * @param key, value
     * @author laughing
     * @date 2018/11/6 17:11
     */
    public void setValue(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setValue(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setValue(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) throws Exception {
        try {
            if (delta < 0) {
                throw new RuntimeException("递增因子必须大于0");
            }
            return redisTemplate.opsForValue().increment(key, delta);
        } finally {
            close();
        }

    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        try {

            if (delta < 0) {
                throw new RuntimeException("递减因子必须大于0");
            }
            return redisTemplate.opsForValue().increment(key, -delta);
        } finally {
            close();
        }
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) throws Exception {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } finally {
            close();
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> getMap(String key) throws Exception {
        try {
            return redisTemplate.opsForHash().entries(key);
        } finally {
            close();
        }
    }

    /**
     * 获取hash中的值
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getMap(String key, Class<T> tClass) {
        try {
            if (key == null || "".equals(key)) {
                return null;
            }
            if (redisTemplate.hasKey(key)) {
                Map<Object, Object> value = redisTemplate.opsForHash().entries(key);
                if (value == null || value.isEmpty()) {
                    return null;
                }
                return JSON.parseObject(JSON.toJSONString(value), tClass);
            }
            return null;
        } finally {
            close();
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean setMap(String key, Map<String, Object> map) throws Exception {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } finally {
            close();
        }
    }

    public boolean setMap(String key, Object object) throws Exception {
        try {
            String json = JSON.toJSONString(object);
            Map<String, Object> map = JSON.parseObject(json, HashMap.class);
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } finally {
            close();
        }
    }

    public boolean setMap(String key, Object object, long time) throws Exception {
        try {
            String json = JSON.toJSONString(object);
            Map<String, Object> map = JSON.parseObject(json, HashMap.class);
            redisTemplate.opsForHash().putAll(key, map);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } finally {
            close();
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hashExistKey(String key, String item) throws Exception {
        try {

            return redisTemplate.opsForHash().hasKey(key, item);
        } finally {
            close();
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) throws Exception {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } finally {
            close();
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) throws Exception {
        try {
            return redisTemplate.opsForHash().increment(key, item, -by);
        } finally {
            close();
        }
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean setSet(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            close();
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            close();
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            close();
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            close();
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            close();
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } finally {
            close();
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } finally {
            close();
        }
    }
}
