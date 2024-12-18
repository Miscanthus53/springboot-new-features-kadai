const stripe = Stripe('pk_test_51QUjQBG4NAbsrbJ8RXJ7aZ4tzkUTT00n1eIsffvG3ZqKw6z4yjH1dUeHM5UWYaTkiXkSilXOcxy8NSYqv8KnHVT800qZ56Jb2u');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
  stripe.redirectToCheckout({
    sessionId: sessionId
  })
});
