function checkComplexity(passwordInput, passwordConf, buttonId) {

    var minLc = document.getElementById("minLc");
    var minUc = document.getElementById("minUc");
    var minLen = document.getElementById("minLen");
    var minNan = document.getElementById("minNan");
    var minNum = document.getElementById("minNum");

    const password = passwordInput.value;
    var submitButton = document.getElementById(buttonId);
    var constraintsFilled = new Map();

    if (passwordConf) {
        var passwordConfVal = document.getElementById(passwordConf).value
        constraintsFilled.set("conf", password === passwordConfVal ? 1 : 0);
    }

    // Check min lower case
    var value = minLc.getAttribute("value")
    if (password.split("").filter((char) => char.match(/[a-z]/)).length >= value) {
        minLc.style.color = "green";
        constraintsFilled.set("minLc", 1);
    }
    else {
        minLc.style.color = "red";
        constraintsFilled.set("minLc", 0);
    }

    // Check min upper case
    value = minUc.getAttribute("value")
    if (password.split("").filter((char) => char.match(/[A-Z]/)).length >= value) {
        minUc.style.color = "green";
        constraintsFilled.set("minUc", 1);
    }
    else {
        minUc.style.color = "red";
        constraintsFilled.set("minUc", 0);
    }

    // Check min length
    value = minLen.getAttribute("value")
    if (password.length >= value) {
        minLen.style.color = "green";
        constraintsFilled.set("minLen", 1);
    }
    else {
        minLen.style.color = "red";
        constraintsFilled.set("minLen", 0);
    }

    // Check min non alphanumeric
    value = minNan.getAttribute("value")
    if (password.split("").filter((char) => char.match(/[\!\?\_\-\#\$\%\&\(\)\^\~]/)).length >= value) {
        minNan.style.color = "green";
        constraintsFilled.set("minNan", 1);
    }
    else {
        minNan.style.color = "red";
        constraintsFilled.set("minNan", 0);
    }

    // Check min numbers
    value = minNum.getAttribute("value")
    if (password.split("").filter((char) => char.match(/[0-9]/)).length >= value) {
        minNum.style.color = "green";
        constraintsFilled.set("minNum", 1);
    }
    else {
        minNum.style.color = "red";
        constraintsFilled.set("minNum", 0);
    }



    for(let value of constraintsFilled.values()) {
        if (parseInt(value) === 0) {
            submitButton.disabled = true;
            return;
        }
    }
    submitButton.disabled = false;
}

function verifyMatch(passwordInput, confirmationInput, submitButton) {
    var password = document.getElementById(passwordInput).value;
    var submitNode = document.getElementById(submitButton);

    submitNode.disabled = confirmationInput.value !== password;
}

function togglePasswordVisibility(passwordFields) {
    passwordFields.forEach(function (passwordField, index) {
        let passwordNode = document.getElementById(passwordField);

        if (passwordNode.type === "password") {
            passwordNode.type = "text";
        } else {
            passwordNode.type = "password";
        }
    });
}