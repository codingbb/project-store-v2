$(document).ready(function() {
    // 가격 표시
    $('.price').each(function(){
        var price = parseInt($(this).text().split(" : ")[1]);
        $(this).text("상품 가격 : " + priceAndQtyString(price));
    });

    // 수량 표시
    $('.qty').each(function(){
        var qty = parseInt($(this).text().split(" : ")[1]);
        $(this).text("상품 수량 : " + priceAndQtyString(qty));
    });

    // 가격과 수량에 콤마를 추가하는 함수
    function priceAndQtyString(priceAndQty) {
        return priceAndQty.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
});