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