<!--    가격 표시 -->
$(document).ready(function() {
    $('.price').each(function(){
        var price = $(this).text();
        let result = priceToString(parseInt(price));
        $(this).html(result);
    });
});

// 수량 표시
function priceToString(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

$(document).ready(function() {
    $('.qty').each(function(){
        var qty = $(this).text();
        let result = priceToString(parseInt(qty));
        $(this).html(result);
    });
});

function qtyToString(qty) {
    return qty.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}