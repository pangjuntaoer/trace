package com.zxq.iov.cloud.trace.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

public class JedisClusterUtils {

	private JedisCluster jc = null;

	private String hostsAndPorts = null;

	private int timeout = 0;

	private int maxRedirections = 0;

	private JedisPoolConfig jedisPoolConfig = null;

	public void init() {
		Set<HostAndPort> hpSet = new HashSet<HostAndPort>();
		StringTokenizer st = new StringTokenizer(hostsAndPorts, ",");
		while (st.hasMoreElements()) {
			String hostAndPort = st.nextToken();
			int idx = hostAndPort.indexOf(":");
			String host = hostAndPort.substring(0, idx);
			int port = Integer.parseInt(hostAndPort.substring(idx + 1));
			HostAndPort hp = new HostAndPort(host, port);
			hpSet.add(hp);
		}
		jc = new JedisCluster(hpSet, timeout, maxRedirections, jedisPoolConfig);
	}

	public String set(String key, String value) {
		return jc.set(key, value);
	}

	public String set(String key, String value, String nxxx, String expx, long time) {
		return jc.set(key, value, nxxx, expx, time);
	}

	public String get(String key) {
		return jc.get(key);
	}

	public Boolean exists(String key) {
		return jc.exists(key);
	}

	public Long exists(String... keys) {
		return jc.exists(keys);
	}

	public Long persist(String key) {
		return jc.persist(key);
	}

	public String type(String key) {
		return jc.type(key);
	}

	public Long expire(String key, int seconds) {
		return jc.expire(key, seconds);
	}

	public Long pexpire(String key, long milliseconds) {
		return jc.pexpire(key, milliseconds);
	}

	public Long expireAt(String key, long unixTime) {
		return jc.expireAt(key, unixTime);
	}

	public Long pexpireAt(String key, long millisecondsTimestamp) {
		return jc.pexpireAt(key, millisecondsTimestamp);
	}

	public Long ttl(String key) {
		return jc.ttl(key);
	}

	public Long pttl(String key) {
		return jc.pttl(key);
	}

	public Boolean setbit(String key, long offset, boolean value) {
		return jc.setbit(key, offset, value);
	}

	public Boolean setbit(String key, long offset, String value) {
		return jc.setbit(key, offset, value);
	}

	public Boolean getbit(String key, long offset) {
		return jc.getbit(key, offset);
	}

	public Long setrange(String key, long offset, String value) {
		return jc.setrange(key, offset, value);
	}

	public String getrange(String key, long startOffset, long endOffset) {
		return jc.getrange(key, startOffset, endOffset);
	}

	public String getSet(String key, String value) {
		return jc.getSet(key, value);
	}

	public Long setnx(String key, String value) {
		return jc.setnx(key, value);
	}

	public String setex(String key, int seconds, String value) {
		return jc.setex(key, seconds, value);
	}

	public String psetex(String key, long milliseconds, String value) {
		return jc.psetex(key, milliseconds, value);
	}

	public Long decrBy(String key, long integer) {
		return jc.decrBy(key, integer);
	}

	public Long decr(String key) {
		return jc.decr(key);
	}

	public Long incrBy(String key, long integer) {
		return jc.incrBy(key, integer);
	}

	public Double incrByFloat(String key, double value) {
		return jc.incrByFloat(key, value);
	}

	public Long incr(String key) {
		return jc.incr(key);
	}

	public Long append(String key, String value) {
		return jc.append(key, value);
	}

	public String substr(String key, int start, int end) {
		return jc.substr(key, start, end);
	}

	public Long hset(String key, String field, String value) {
		return jc.hset(key, field, value);
	}

	public String hget(String key, String field) {
		return jc.hget(key, field);
	}

	public Long hsetnx(String key, String field, String value) {
		return jc.hsetnx(key, field, value);
	}

	public String hmset(String key, Map<String, String> hash) {
		return jc.hmset(key, hash);
	}

	public List<String> hmget(String key, String... fields) {
		return jc.hmget(key, fields);
	}

	public Long hincrBy(String key, String field, long value) {
		return jc.hincrBy(key, field, value);
	}

	public Double hincrByFloat(String key, String field, double value) {
		return jc.hincrByFloat(key, field, value);
	}

	public Boolean hexists(String key, String field) {
		return jc.hexists(key, field);
	}

	public Long hdel(String key, String... field) {
		return jc.hdel(key, field);
	}

	public Long hlen(String key) {
		return jc.hlen(key);
	}

	public Set<String> hkeys(String key) {
		return jc.hkeys(key);
	}

	public List<String> hvals(String key) {
		return jc.hvals(key);
	}

	public Map<String, String> hgetAll(String key) {
		return jc.hgetAll(key);
	}

	public Long rpush(String key, String... string) {
		return jc.rpush(key, string);
	}

	public Long lpush(String key, String... string) {
		return jc.lpush(key, string);
	}

	public Long llen(String key) {
		return jc.llen(key);
	}

	public List<String> lrange(String key, long start, long end) {
		return jc.lrange(key, start, end);
	}

	public String ltrim(String key, long start, long end) {
		return jc.ltrim(key, start, end);
	}

	public String lindex(String key, long index) {
		return jc.lindex(key, index);
	}

	public String lset(String key, long index, String value) {
		return jc.lset(key, index, value);
	}

	public Long lrem(String key, long count, String value) {
		return jc.lrem(key, count, value);
	}

	public String lpop(String key) {
		return jc.lpop(key);
	}

	public String rpop(String key) {
		return jc.rpop(key);
	}

	public Long sadd(String key, String... member) {
		return jc.sadd(key, member);
	}

	public Set<String> smembers(String key) {
		return jc.smembers(key);
	}

	public Long srem(String key, String... member) {
		return jc.srem(key, member);
	}

	public String spop(String key) {
		return jc.spop(key);
	}

	public Set<String> spop(String key, long count) {
		return jc.spop(key, count);
	}

	public Long scard(String key) {
		return jc.scard(key);
	}

	public Boolean sismember(String key, String member) {
		return jc.sismember(key, member);
	}

	public String srandmember(String key) {
		return jc.srandmember(key);
	}

	public List<String> srandmember(String key, int count) {
		return jc.srandmember(key, count);
	}

	public Long strlen(String key) {
		return jc.strlen(key);
	}

	public Long zadd(String key, double score, String member) {
		return jc.zadd(key, score, member);
	}

	public Long zadd(String key, double score, String member, ZAddParams params) {
		return jc.zadd(key, score, member, params);
	}

	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return jc.zadd(key, scoreMembers);
	}

	public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
		return jc.zadd(key, scoreMembers, params);
	}

	public Set<String> zrange(String key, long start, long end) {
		return jc.zrange(key, start, end);
	}

	public Long zrem(String key, String... member) {
		return jc.zrem(key, member);
	}

	public Double zincrby(String key, double score, String member) {
		return jc.zincrby(key, score, member);
	}

	public Double zincrby(String key, double score, String member, ZIncrByParams params) {
		return jc.zincrby(key, score, member, params);
	}

	public Long zrank(String key, String member) {
		return jc.zrank(key, member);
	}

	public Long zrevrank(String key, String member) {
		return jc.zrevrank(key, member);
	}

	public Set<String> zrevrange(String key, long start, long end) {
		return jc.zrevrange(key, start, end);
	}

	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		return jc.zrangeWithScores(key, start, end);
	}

	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		return jc.zrevrangeWithScores(key, start, end);
	}

	public Long zcard(String key) {
		return jc.zcard(key);
	}

	public Double zscore(String key, String member) {
		return jc.zscore(key, member);
	}

	public List<String> sort(String key) {
		return jc.sort(key);
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		return jc.sort(key, sortingParameters);
	}

	public Long zcount(String key, double min, double max) {
		return jc.zcount(key, min, max);
	}

	public Long zcount(String key, String min, String max) {
		return jc.zcount(key, min, max);
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		return jc.zrangeByScore(key, min, max);
	}

	public Set<String> zrangeByScore(String key, String min, String max) {
		return jc.zrangeByScore(key, min, max);
	}

	public Set<String> zrevrangeByScore(String key, double max, double min) {
		return jc.zrevrangeByScore(key, max, min);
	}

	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		return jc.zrangeByScore(key, min, max, offset, count);
	}

	public Set<String> zrevrangeByScore(String key, String max, String min) {
		return jc.zrevrangeByScore(key, max, min);
	}

	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		return jc.zrangeByScore(key, min, max, offset, count);
	}

	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		return jc.zrevrangeByScore(key, max, min, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		return jc.zrangeByScoreWithScores(key, min, max);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		return jc.zrevrangeByScoreWithScores(key, max, min);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		return jc.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		return jc.zrevrangeByScore(key, max, min, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		return jc.zrangeByScoreWithScores(key, min, max);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
		return jc.zrevrangeByScoreWithScores(key, max, min);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
		return jc.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		return jc.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
		return jc.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	public Long zremrangeByRank(String key, long start, long end) {
		return jc.zremrangeByRank(key, start, end);
	}

	public Long zremrangeByScore(String key, double start, double end) {
		return jc.zremrangeByScore(key, start, end);
	}

	public Long zremrangeByScore(String key, String start, String end) {
		return jc.zremrangeByScore(key, start, end);
	}

	public Long zlexcount(String key, String min, String max) {
		return jc.zlexcount(key, min, max);
	}

	public Set<String> zrangeByLex(String key, String min, String max) {
		return jc.zrangeByLex(key, min, max);
	}

	public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
		return jc.zrangeByLex(key, min, max, offset, count);
	}

	public Set<String> zrevrangeByLex(String key, String max, String min) {
		return jc.zrevrangeByLex(key, max, min);
	}

	public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
		return jc.zrevrangeByLex(key, max, min, offset, count);
	}

	public Long zremrangeByLex(String key, String min, String max) {
		return jc.zremrangeByLex(key, min, max);
	}

	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		return jc.linsert(key, where, pivot, value);
	}

	public Long lpushx(String key, String... string) {
		return jc.lpushx(key, string);
	}

	public Long rpushx(String key, String... string) {
		return jc.rpushx(key, string);
	}

	public Long del(String key) {
		return jc.del(key);
	}

	public String echo(String string) {
		return jc.echo(string);
	}

	public Long bitcount(String key) {
		return jc.bitcount(key);
	}

	public Long bitcount(String key, long start, long end) {
		return jc.bitcount(key, start, end);
	}

	public Long bitpos(String key, boolean value) {
		return jc.bitpos(key, value);
	}

	public Long bitpos(String key, boolean value, BitPosParams params) {
		return jc.bitpos(key, value, params);
	}

	public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
		return jc.hscan(key, cursor);
	}

	public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
		return jc.hscan(key, cursor, params);
	}

	public ScanResult<String> sscan(String key, String cursor) {
		return jc.sscan(key, cursor);
	}

	public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
		return jc.sscan(key, cursor, params);
	}

	public ScanResult<Tuple> zscan(String key, String cursor) {
		return jc.zscan(key, cursor);
	}

	public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
		return jc.zscan(key, cursor, params);
	}

	public Long pfadd(String key, String... elements) {
		return jc.pfadd(key, elements);
	}

	public long pfcount(String key) {
		return jc.pfcount(key);
	}

	public List<String> blpop(int timeout, String key) {
		return jc.blpop(timeout, key);
	}

	public List<String> brpop(int timeout, String key) {
		return jc.brpop(timeout, key);
	}

	public Long del(String... keys) {
		return jc.del(keys);
	}

	public List<String> blpop(int timeout, String... keys) {
		return jc.blpop(timeout, keys);

	}

	public List<String> brpop(int timeout, String... keys) {
		return jc.brpop(timeout, keys);
	}

	public List<String> mget(String... keys) {
		return jc.mget(keys);
	}

	public String mset(String... keysvalues) {
		return jc.mset(keysvalues);
	}

	public Long msetnx(String... keysvalues) {
		return jc.msetnx(keysvalues);
	}

	public String rename(String oldkey, String newkey) {
		return jc.rename(oldkey, newkey);
	}

	public Long renamenx(String oldkey, String newkey) {
		return jc.renamenx(oldkey, newkey);
	}

	public String rpoplpush(String srckey, String dstkey) {
		return jc.rpoplpush(srckey, dstkey);
	}

	public Set<String> sdiff(String... keys) {
		return jc.sdiff(keys);
	}

	public Long sdiffstore(String dstkey, String... keys) {
		return jc.sdiffstore(dstkey, keys);
	}

	public Set<String> sinter(String... keys) {
		return jc.sinter(keys);
	}

	public Long sinterstore(String dstkey, String... keys) {
		return jc.sinterstore(dstkey, keys);
	}

	public Long smove(String srckey, String dstkey, String member) {
		return jc.smove(srckey, dstkey, member);
	}

	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		return jc.sort(key, sortingParameters, dstkey);
	}

	public Long sort(String key, String dstkey) {
		return jc.sort(key, dstkey);
	}

	public Set<String> sunion(String... keys) {
		return jc.sunion(keys);
	}

	public Long sunionstore(String dstkey, String... keys) {
		return jc.sunionstore(dstkey, keys);
	}

	public Long zinterstore(String dstkey, String... sets) {
		return jc.zinterstore(dstkey, sets);
	}

	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		return jc.zinterstore(dstkey, params, sets);
	}

	public Long zunionstore(String dstkey, String... sets) {
		return jc.zunionstore(dstkey, sets);
	}

	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		return jc.zunionstore(dstkey, params, sets);
	}

	public String brpoplpush(String source, String destination, int timeout) {
		return jc.brpoplpush(source, destination, timeout);
	}

	public Long publish(String channel, String message) {
		return jc.publish(channel, message);
	}

	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		jc.subscribe(jedisPubSub, channels);
	}

	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		jc.psubscribe(jedisPubSub, patterns);
	}

	public Long bitop(BitOP op, String destKey, String... srcKeys) {
		return jc.bitop(op, destKey, srcKeys);
	}

	public String pfmerge(String destkey, String... sourcekeys) {
		return jc.pfmerge(destkey, sourcekeys);
	}

	public long pfcount(String... keys) {
		return jc.pfcount(keys);
	}

	public Object eval(String script, int keyCount, String... params) {
		return jc.eval(script, keyCount, params);
	}

	public Object eval(String script, String key) {
		return jc.eval(script, key);
	}

	public Object eval(String script, List<String> keys, List<String> args) {
		return jc.eval(script, keys, args);
	}

	public Object evalsha(String sha1, int keyCount, String... params) {
		return jc.evalsha(sha1, keyCount, params);
	}

	public Object evalsha(String sha1, List<String> keys, List<String> args) {
		return jc.evalsha(sha1, keys, args);
	}

	public Object evalsha(String script, String key) {
		return jc.evalsha(script, key);
	}

	public Boolean scriptExists(String sha1, String key) {
		return jc.scriptExists(sha1, key);
	}

	public List<Boolean> scriptExists(String key, String... sha1) {
		return jc.scriptExists(key, sha1);
	}

	public String scriptLoad(String script, String key) {
		return jc.scriptLoad(script, key);
	}

	@Deprecated
	public String set(String key, String value, String nxxx) {
		return jc.set(key, value, nxxx);
	}

	@Deprecated
	public List<String> blpop(String arg) {
		return jc.blpop(arg);
	}

	@Deprecated
	public List<String> brpop(String arg) {
		return jc.brpop(arg);
	}

	@Deprecated
	public Long move(String key, int dbIndex) {
		return jc.move(key, dbIndex);
	}

	@Deprecated
	public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
		return jc.hscan(key, cursor);
	}

	@Deprecated
	public ScanResult<String> sscan(String key, int cursor) {
		return jc.sscan(key, cursor);
	}

	@Deprecated
	public ScanResult<Tuple> zscan(String key, int cursor) {
		return jc.zscan(key, cursor);
	}

	public Long geoadd(String key, double longitude, double latitude, String member) {
		return jc.geoadd(key, longitude, latitude, member);
	}

	public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
		return jc.geoadd(key, memberCoordinateMap);
	}

	public Double geodist(String key, String member1, String member2) {
		return jc.geodist(key, member1, member2);
	}

	public Double geodist(String key, String member1, String member2, GeoUnit unit) {
		return jc.geodist(key, member1, member2, unit);
	}

	public List<String> geohash(String key, String... members) {
		return jc.geohash(key, members);
	}

	public List<GeoCoordinate> geopos(String key, String... members) {
		return jc.geopos(key, members);
	}

	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
			GeoUnit unit) {
		return jc.georadius(key, longitude, latitude, radius, unit);
	}

	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		return jc.georadius(key, longitude, latitude, radius, unit, param);
	}

	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
		return jc.georadiusByMember(key, member, radius, unit);
	}

	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		return jc.georadiusByMember(key, member, radius, unit, param);
	}

	public String getHostsAndPorts() {
		return hostsAndPorts;
	}

	public void setHostsAndPorts(String hostsAndPorts) {
		this.hostsAndPorts = hostsAndPorts;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMaxRedirections() {
		return maxRedirections;
	}

	public void setMaxRedirections(int maxRedirections) {
		this.maxRedirections = maxRedirections;
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return jedisPoolConfig;
	}

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

}
