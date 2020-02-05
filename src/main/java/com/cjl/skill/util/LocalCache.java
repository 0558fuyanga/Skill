package com.cjl.skill.util;

import java.util.concurrent.ConcurrentHashMap;

public class LocalCache {
	//定义已售完商品本地缓存
	public static ConcurrentHashMap<Integer, Boolean> soldOutProducts = new ConcurrentHashMap<>();
}
