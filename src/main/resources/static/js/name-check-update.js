// 실시간 상품명 중복체크
$("#name").keyup(function (){
    //this = 지금 현재 클릭한 것, val = 값 가져옴
    var name = $(this).val();
    var productId = $("input[name='productId']").val(); // 상품 ID 가져오기
    // alert(name);

    //서버로 가서 id 중복체크 -> url과 입력 데이터는 바뀌면 안됨 -> Ajax
    //url -> /product/name-check
    //서버에서 전달되는 데이터를 result로 받음 -> 가져온 데이터가 null이면 사용 가능, 있으면 중복
    var encodedName = encodeURIComponent(name); //이게 없으면 띄어쓰기 인식 안됨

    $.ajax({
        method: "GET",
        url: "/product/name-check/update?name=" + encodedName + "&id=" + productId

    }).done((res)=>{
        console.log(res);
        if (res.body === true) {    //res의 body값이랑 비교해야함!!
            $("#nameCheck").removeClass("alert-danger");
            $("#nameCheck").addClass("alert-success");
            $("#nameCheck").text("사용 가능한 상품명 입니다.");
        } else {
            $("#nameCheck").removeClass("alert-success");
            $("#nameCheck").addClass("alert-danger");
            $("#nameCheck").text("중복된 상품명 입니다.");
        }
    }).fail((res)=>{
        alert("통신 오류");
    });

});

$(document).ready(function() {
    // 가격 표시
    var price = $('input[name="price"]').val();
    $('input[name="price"]').val(priceToString(parseInt(price)));

    // 수량 표시
    var qty = $('input[name="qty"]').val();
    $('input[name="qty"]').val(qtyToString(parseInt(qty)));
});

function priceToString(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function qtyToString(qty) {
    return qty.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

$(document).ready(function() {
    // 가격 입력 필드에 대한 이벤트 리스너 추가
    $('#price').on('input', function() {
        // 입력된 값에서 숫자만 추출
        var number = $(this).val().replace(/[^0-9]/g, '');
        // 천 단위 구분자 적용
        var changeNum = number.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        // 변환된 값으로 입력 필드 업데이트
        $(this).val(changeNum);

    });

    $('#qty').on('input', function() {
        // 입력된 값에서 숫자만 추출
        var number = $(this).val().replace(/[^0-9]/g, '');
        // 천 단위 구분자 적용
        var changeNum = number.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        // 변환된 값으로 입력 필드 업데이트
        $(this).val(changeNum);
    });


    // 폼 제출 전 실행될 이벤트
    $('form').on('submit', function() {
        // 가격과 수량 입력 필드에서 천 단위 구분자 제거
        var price = $('#price').val().replace(/,/g, '');
        $('#price').val(price);
        // 수량 필드에 대해서도 같은 처리를 수행
        var qty = $('input[name="qty"]').val().replace(/,/g, '');
        $('input[name="qty"]').val(qty);
    });
});
