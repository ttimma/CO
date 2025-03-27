document.addEventListener('DOMContentLoaded', () => {
  // Retrieve the flight and fare information stored in sessionStorage.
  const flightFareInfo = JSON.parse(sessionStorage.getItem('selectedFlightFare'));
  // Retrieve the chosen seats from sessionStorage.
  const chosenSeats = JSON.parse(sessionStorage.getItem('chosenSeats')) || [];
  // Retrieve the passengers from sessionStorage.
  const passengers = JSON.parse(sessionStorage.getItem('passengers')) || [];

  let summaryHTML = '';

  // Flight details
  if (flightFareInfo && flightFareInfo.flight) {
    summaryHTML += `<p><strong>Flight Number:</strong> ${flightFareInfo.flight.flightNumber}</p>`;
    summaryHTML += `<p><strong>Destination:</strong> ${flightFareInfo.flight.to}</p>`;
    summaryHTML += `<p><strong>Fare Option:</strong> ${flightFareInfo.fare}</p>`;
    summaryHTML += `<p><strong>Price:</strong> €${flightFareInfo.price}</p>`;
  } else {
    summaryHTML += `<p>No flight information available.</p>`;
  }

  // Display chosen seats
  if (chosenSeats.length > 0) {
    summaryHTML += `<h4>Your Seats:</h4><ul>`;
    const seatLabels = ['A', 'B', 'C', 'D', 'E', 'F'];
    chosenSeats.forEach((seat, index) => {
      const seatLabel = `${seat.row}${seatLabels[seat.column - 1]}`;
      summaryHTML += `<li>Seat for Passenger ${index + 1}: ${seatLabel}</li>`;
    });
    summaryHTML += `</ul>`;
  } else {
    summaryHTML += `<p>No seats were selected.</p>`;
  }

  // Display passenger names
  if (passengers.length > 0) {
    summaryHTML += `<h4>Passenger Names:</h4><ul>`;
    passengers.forEach((p, index) => {
      summaryHTML += `<li>Passenger ${index + 1}: ${p.firstName} ${p.lastName}</li>`;
    });
    summaryHTML += `</ul>`;
  }

  summaryHTML += `<p class="mt-4 confirmation">Your booking has been confirmed. Thank you for choosing our service!</p>`;

  document.getElementById('booking-summary').innerHTML = summaryHTML;
});
