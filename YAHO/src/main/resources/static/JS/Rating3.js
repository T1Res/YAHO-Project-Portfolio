// rating.js
let userProfileImg = '/IMG/kibon_image.jpg';
function submitRate() {
    const score = getSelectedScore();
    const content = getContent("#registerFormArea textarea");
    const animeId = getInputValue("register_anime_id");
    const userId = getInputValue("register_user_id");

	
    if (!score || !content ) {
        alert("별점, 댓글을 모두 입력해주세요!");
        return;
    }
	if(!userId){
		alert("로그인 해주세요!");
		return;
	}

    fetch("/Schedule/ajaxInsert", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ SCORE_SCORE: score, SCORE_CONTENT: content, ANIME_ID: animeId, USER_ID: userId })
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert("등록 완료!");
            refreshRateUI(animeId, userId);
			refreshGradeStats(animeId);
			refreshAnimeScore(animeId, score); // 👉 평점 텍스트 반영
        } else {
            alert("등록 실패!");
        }
    });
}

function updateRate() {
    const score = getSelectedScore();
    const content = getContent("#rateFormArea textarea");
    const animeId = getInputValue("rated_anime_id");
    const userId = getInputValue("rated_user_id");

	
	console.log("스코어"+score);
	console.log("컨텐츠"+content);
	console.log("아니메아이디"+animeId);
	console.log("유저아이디"+userId);
		
    if (!score || !content || !animeId || !userId) {
        alert("모든 정보를 입력해주세요.");
        return;
    }

    fetch("/Schedule/ajaxUpdate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ SCORE_SCORE: score, SCORE_CONTENT: content, ANIME_ID: animeId, USER_ID: userId })
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert("수정 완료!");
            refreshRateUI(animeId, userId);
			refreshGradeStats(animeId);
			refreshAnimeScore(animeId, score); // 👉 평점 텍스트 반영
        } else {
            alert("수정 실패!");
        }
    });
}

function deleteRate() {
    const animeId = getInputValue("rated_anime_id");
    const userId = getInputValue("rated_user_id");

    if (!animeId || !userId) {
        alert("ID 정보가 없습니다.");
        return;
    }

    if (!confirm("정말 삭제하시겠습니까?")) return;

    fetch("/Schedule/ajaxDelete", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ANIME_ID: animeId, USER_ID: userId })
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert("삭제 완료!");
			refreshGradeStats(animeId);
			refreshRateUI(animeId, userId);
        } else {
            alert("삭제 실패!");
        }
    });
}

function refreshRateUI(animeId, userId) {
    fetch("/Schedule/rateInfoJson", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ANIME_ID: animeId })
    })
    .then(res => res.json())
    .then(data => {
        updateBarChart(data.scoreList, data.countList);
        updateDonutChart(data.maleRatio, data.femaleRatio);
        updateCommentList(data.list);

        // userProfileImg 값 유지
        userProfileImg = data.userProfileImg || userProfileImg;

        applyUserRatingForm(data.Aldto, animeId, userId, userProfileImg); // ✅ 프로필 이미지 넘김
    });
}

function updateBarChart(scoreList, countList) {
    const canvas = document.getElementById("myChart");
    if (!canvas) return;

    const ctx = canvas.getContext("2d");
    if (window.myChart) window.myChart.destroy();

    window.myChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: scoreList,
            datasets: [{
                label: "평점 분포",
                data: countList,
                backgroundColor: ["#f66", "#69f", "#fc6", "#6cc", "#c6f"],
                borderColor: ["#f00", "#00f", "#f90", "#0cc", "#90f"],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: { y: { beginAtZero: true } }
        }
    });
}

function updateDonutChart(male, female) {
    const canvas = document.getElementById("genderChart");
    if (!canvas) return;

    const ctx = canvas.getContext("2d");
    if (window.genderChart) window.genderChart.destroy();

    window.genderChart = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: ["남성", "여성"],
            datasets: [{
                data: [male, female],
                backgroundColor: ["#36A2EB", "#FF6384"]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

function updateCommentList(list) {
    const area = document.getElementById("comment-list-area");

    area.innerHTML = "<h1>게시판 목록</h1>";

    if (list.length === 0) {
        area.innerHTML += `<p style="color:gray;">아직 댓글이 없습니다.</p>`;
        return;
    }

    list.forEach(dto => {
        const div = document.createElement("div");
        div.className = "comment-card";
        div.innerHTML = `
            <div class="user-id"><strong>${dto.USER_ID}</strong></div>
            <div class="stars">${generateStars(dto.SCORE_SCORE)}</div>
            <div class="content">${dto.SCORE_CONTENT}</div>
        `;
        area.appendChild(div);
    });
}

function generateStars(score) {
    const full = Math.floor(score);
    const half = score % 1 >= 0.5 ? 1 : 0;
    const empty = 5 - full - half;

    return '⭐'.repeat(full) + (half ? '✨' : '') + '✩'.repeat(empty);
}

// ✨ 유틸 함수들 ✨
function getInputValue(id) {
    return document.getElementById(id)?.value || "";
}

function getContent(selector) {
    return document.querySelector(selector)?.value.trim() || "";
}

function getSelectedScore() {
    return document.querySelector('input[name="SCORE_SCORE"]:checked')?.value || null;
}
//평점 텍스트 점수 바꾸기
function refreshAnimeScore(animeId, newScore) {
    const card = document.querySelector(`.anime-card[data-anime-id="${animeId}"]`);
    if (card) {
        const ratingDiv = card.querySelector(".anime-rating");
        if (ratingDiv) {
            ratingDiv.textContent = `⭐ ${newScore}`;
        }

        // data-rating 속성도 같이 갱신 (필요한 경우 대비)
        card.setAttribute("data-rating", newScore);
    }
}
function refreshGradeStats(animeId) {
    fetch("/Schedule/rateInfoJson", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ANIME_ID: animeId })
    })
    .then(res => res.json())
    .then(data => {
        document.getElementById("grade-number").textContent = data.grade;
        document.getElementById("grade-mark").textContent = data.gradeMark;
        updateBarChart(data.scoreList, data.countList);
        updateDonutChart(data.maleRatio, data.femaleRatio);
        updateCommentList(data.list);

        userProfileImg = data.userProfileImg || userProfileImg;

        applyUserRatingForm(data.Aldto, animeId, data.USER_ID, userProfileImg); // ✅ 수정된 부분
    });
}
// 전역 등록
window.submitRate = submitRate;
window.updateRate = updateRate;
window.deleteRate = deleteRate;