
document.addEventListener("DOMContentLoaded", function() {
    // imgFile input 필드에 event listener 추가
    document.getElementById('imgFile').addEventListener('change', function(event) {
        var output = document.querySelector('img'); // 미리보기를 할 이미지 태그 선택
        if (event.target.files && event.target.files[0]) {
            // FileReader 객체를 이용해 파일을 읽음
            var reader = new FileReader();

            reader.onload = function(e) {
                output.src = e.target.result; // 읽은 파일의 내용을 img 태그의 src 속성에 할당
                output.style.display = 'block'; // 이미지 태그를 화면에 표시
            };

            reader.readAsDataURL(event.target.files[0]); // 파일 읽기 시작
        }
    });
});