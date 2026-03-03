document.addEventListener("DOMContentLoaded", () => {
    const playAudio = window.playAudioFlag; // 컨트롤러에서 넘겨준 변수
    console.log("🎧 [audio-play.js] playAudioFlag:", playAudio);

    if (playAudio) {
        const mp3Files = [
            "/ljj/audio/yahojhr.mp3",
            "/ljj/audio/yahoasy.mp3",
            "/ljj/audio/yahokrw.mp3",
            "/ljj/audio/yahosgm.mp3",
            "/ljj/audio/yaholjj.mp3"
        ];

        const randomSrc = mp3Files[Math.floor(Math.random() * mp3Files.length)];
        const audio = document.getElementById("audioPlayer");
        if (audio) {
            audio.src = randomSrc;
            audio.play()
                .then(() => console.log("🎵 오디오 재생됨:", randomSrc))
                .catch(err => console.error("❌ 오디오 재생 실패:", err));
        }
    }
});
