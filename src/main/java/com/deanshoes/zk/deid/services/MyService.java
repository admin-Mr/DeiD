package com.deanshoes.zk.deid.services;

import java.util.List;

import com.deanshoes.zk.deid.entity.Log;

public interface MyService {

	Log addLog(Log log);

	List<Log> getLogs();

	void deleteLog(Log log);
}
