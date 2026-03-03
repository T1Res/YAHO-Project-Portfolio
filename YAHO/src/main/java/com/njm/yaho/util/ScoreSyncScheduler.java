package com.njm.yaho.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.njm.yaho.service.admin.AnimeService;

@Component
public class ScoreSyncScheduler {
	@Autowired
	private AnimeService animeService;
	
	private static final String LOG_FILE_PATH = "logs/score-sync.log"; // 저장할 파일 경로
	
	@Scheduled(fixedRate = 300000) // 5분마다
	public void syncScores() {
		try {
            writeLog("[M] MySQL 평점 동기화 시작");

            animeService.syncScore();

            writeLog("[M] MySQL 평점 동기화 완료 ✅");
        } catch (Exception e) {
            writeLog("[M] MySQL 평점 동기화 실패 ❌ : " + e.getMessage());
        }
	}
	
	@Scheduled(fixedRate = 3600000) // 1시간마다
	public void calcScores() {
		try {
            writeLog("[O] Oracle DB 평점 계산 시작");

            animeService.scoreAveCal();

            writeLog("[O] Oracle DB 평점 계산 완료 ✅");
        } catch (Exception e) {
            writeLog("[O] Oracle DB 평점 계산 실패 ❌ : " + e.getMessage());
        }
	}
	
	// 로그를 파일에 기록하는 메소드
	private void writeLog(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            out.println(getFormattedNow() + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	// 타임스탬프 포맷
	private String getFormattedNow() {
	    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
