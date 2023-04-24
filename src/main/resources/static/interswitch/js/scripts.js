// confirm payment from the back-end
function confirmPayment(response) {
    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(response)
    };
    fetch('http://localhost:8080/confirm-payment', options)
        .then(response => response.json())
        .then(data => "00" == data.ResponseCode ?
            showAlert("success", data.ResponseDescription) :
            showAlert("error", "Payment failed: " + data.ResponseDescription))
        .catch(error => showAlert("error", "An error occurred while confirming transaction"));
}

// declare callback function
function paymentCallback(response) {
    console.log(response);
    switch (response.resp) {
        case "00":
            confirmPayment(response);
            break;
        case "Z6":
            showAlert("error", "Payment Cancelled");
            break;
        default:
            var reason = response.desc ? ": " + response.desc : "";
            showAlert("error", "Transaction Failed" + reason);
    }
}

function inlineCheckout() {
    const form = document.getElementById("payment-form");
    const formData = new FormData(form);
    const paymentRequest = {
        onComplete: paymentCallback,
        mode: "TEST"
    };
    for (const [key, value] of formData.entries()) {
        paymentRequest[key] = value;
    }
    //call webpayCheckout to initiate the payment
    window.webpayCheckout(paymentRequest);
}

function showAlert(type, message) {
    var alertDiv = document.getElementById('alert');
    alertDiv.classList.remove('alert', 'alert-success', 'alert-danger');
    if (type === 'success') {
        alertDiv.classList.add('alert', 'alert-success');
    } else if (type === 'error') {
        alertDiv.classList.add('alert', 'alert-danger');
    }
    alertDiv.innerText = message;
}