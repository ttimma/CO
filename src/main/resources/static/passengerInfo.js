// passengerinfo.js

import { createReservation } from './api.js';
import { renderPassengerFields, getPassengersFromForm } from './ui.js';

document.addEventListener('DOMContentLoaded', () => {
  // Retrieve chosen seats from sessionStorage (set during seat selection)
  const chosenSeats = JSON.parse(sessionStorage.getItem('chosenSeats')) || [];
  
  // Render passenger input fields into the container with id 'passengerFields'
  renderPassengerFields(chosenSeats, 'passengerFields');

  // Set up form submission handler
  const passengerForm = document.getElementById('passengerForm');
  passengerForm.addEventListener('submit', (e) => {
    e.preventDefault();

    let passengers;
    try {
      // Read passenger info from form fields
      passengers = getPassengersFromForm(chosenSeats.length);
    } catch (error) {
      alert(error.message);
      return;
    }

    // Retrieve selected flight and fare info from sessionStorage
    const flightFareInfo = JSON.parse(sessionStorage.getItem('selectedFlightFare'));
    if (!flightFareInfo || !flightFareInfo.flight) {
      alert("Flight information is missing. Please select a flight.");
      return;
    }
    const routeId = flightFareInfo.flight.id;
    if (!routeId) {
      alert("Flight route ID is missing.");
      return;
    }
    const priceListId = flightFareInfo.flight.priceListId;
    if (!priceListId) {
      alert("A valid Price List ID is missing.");
      return;
    }

    // Build the reservation request payload
    const reservationRequest = {
      priceListId: priceListId,
      passengers: passengers,
      routeIds: [routeId]
    };

    console.log("Reservation JSON:", JSON.stringify(reservationRequest));

    // Call the API to create the reservation
    createReservation(reservationRequest)
      .then(data => {
        // Optionally store passengers in sessionStorage for later display
        sessionStorage.setItem('passengers', JSON.stringify(passengers));
        document.getElementById('reservationMessage').innerHTML = `
          <p class="text-success">
            Reservation created! ID: ${data.id}, Total Price: ${data.totalPrice}, Created At: ${data.createdAt}
          </p>
        `;
        // Redirect to the booking confirmation page
        window.location.href = 'bookingConfirmation.html';
      })
      .catch(err => {
        document.getElementById('reservationMessage').innerHTML = `<p class="text-danger">Error creating reservation: ${err.message}</p>`;
      });
  });
});
