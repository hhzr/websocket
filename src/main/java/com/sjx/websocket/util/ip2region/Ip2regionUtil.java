package com.sjx.websocket.util.ip2region;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * SunJingxuan
 * 2022 \ 07 \ 28
 */
@Slf4j
@Component
public class Ip2regionUtil {

	public Map<String, Object> getIPTerritory(String ip) {
//		String dbPath = "/root/ip2region.xdb";
		String dbPath = "E:\\ip2region.xdb";

		// 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
		byte[] vIndex;
		try {
			vIndex = Searcher.loadVectorIndexFromFile(dbPath);
		} catch (Exception e) {
			log.error("failed to load vector index from `{}`: {}", dbPath, e);
			return null;
		}

		// 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
		Searcher searcher;
		try {
			searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
		} catch (Exception e) {
			log.error("failed to create vectorIndex cached searcher with `{}`: {}", dbPath, e);
			return null;
		}

		// 3、查询
		try {
			long sTime = System.nanoTime();
			String region = searcher.search(ip);
			long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
			log.info("{region: {}, ioCount: {}, took: {} μs}", region, searcher.getIOCount(), cost);
			Map<String, Object> map = new HashMap<>();
			String[] split = region.split("\\|");
			map.put("region", region);
			map.put("country", split[0]);
			map.put("province", split[2]);
			map.put("city", split[3]);
			map.put("serviceProvider", split[4]);
			map.put("ioCount", searcher.getIOCount());
			map.put("took", cost + "μs");
			return map;
		} catch (Exception e) {
			log.error("failed to search({}): {}", ip, e);
		}
		return null;
		// 备注：每个线程需要单独创建一个独立的 Searcher 对象，但是都共享全局的制度 vIndex 缓存。
	}
}
