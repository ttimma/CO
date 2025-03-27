
  // ------------------ FLIGHTS API Functions ---------------//

export function fetchOrigins() {
  return fetch("http://localhost:8080/api/origins")
    .then(response => {
      if (!response.ok) {
        throw new Error(`Error fetching origins: ${response.status}`);
      }
      return response.json();
    });
}

export function fetchDestinations() {
  return fetch("http://localhost:8080/api/destinations")
    .then(response => {
      if (!response.ok) {
        throw new Error(`Error fetching destinations: ${response.status}`);
      }
      return response.json();
    });
}

export function fetchCompanies() {
  return fetch("http://localhost:8080/api/companies")
    .then(response => {
      if (!response.ok) {
        throw new Error(`Error fetching companies: ${response.status}`);
      }
      return response.json();
    });
}

export function fetchRoutes(queryParams) {
  return fetch(`http://localhost:8080/api/routes${queryParams}`)
    .then(response => {
      if (!response.ok) {
        throw new Error(`Server error: ${response.status}`);
      }
      return response.json();
    });
}

export function updatePriceList() {
  return fetch("http://localhost:8080/api/pricelists/update")
    .then(response => {
      if (!response.ok) {
        throw new Error(`Error updating PriceList: ${response.status}`);
      }
      return response.json();
    });
}

  // ------------------ SEATS API Functions ------------------
/**
 * Fetch the seat data for a specific flight from the server.
 * Returns a promise that resolves to an array of seat objects.
 */
export function fetchSeatsApi() {
  // Retrieve the selected flight info from session storage.
  const flightData = JSON.parse(sessionStorage.getItem('selectedFlightFare'));
  const flightNumber = flightData.flight.flightNumber;
  
  // Build the URL using the flight number.
  const url = `http://localhost:8080/flights/${flightNumber}/seats`;
  return fetch(url)
    .then(response => {
      if (!response.ok) {
        throw new Error("Error fetching seats");
      }
      return response.json();
    });
}

  /**
 * Book a single seat for the specified flight.
 * @param {number} row - The row number of the seat.
 * @param {number} col - The column number of the seat.
 * Returns a promise that resolves to a confirmation message.
 */
  export function bookSeatApi(row, col) {
    const flightData = JSON.parse(sessionStorage.getItem('selectedFlightFare'));
    const flightNumber = flightData.flight.flightNumber;
    
    // Use the flight number in the endpoint URL.
    return fetch(`http://localhost:8080/flights/${flightNumber}/seats/book`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ row, col })
    }).then(response => {
      if (!response.ok) {
        throw new Error("Error booking seat");
      }
      return response.text();
    });
  }
  
  
  /**
 * Confirm (book) multiple seats for the specified flight.
 * @param {Array} selectedSeats - An array of seat objects selected by the user.
 * Returns a promise that resolves when all booking requests complete.
 */
  export function confirmSeatsApi(selectedSeats) {
    const flightData = JSON.parse(sessionStorage.getItem('selectedFlightFare'));
    const flightNumber = flightData.flight.flightNumber;
    
    // Build the URL with the flight number for each booking.
    return Promise.all(
      selectedSeats.map(seat =>
        fetch(`http://localhost:8080/flights/${flightNumber}/seats/book`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ row: seat.row, col: seat.column })
        }).then(response => {
          if (!response.ok) {
            throw new Error("Error booking one of the seats");
          }
          return response.text();
        })
      )
    );
  }
  

// ---------------- RESERVATION API ---------------//
// reservationApi.js

/**
 * Sends a POST request to create a reservation.
 * @param {Object} reservationRequest - The reservation request payload.
 * @returns {Promise<Object>} - A promise that resolves to the created reservation data.
 */
export function createReservation(reservationRequest) {
  return fetch('http://localhost:8080/api/reservations', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(reservationRequest)
  }).then(response => {
    if (!response.ok) {
      return response.json().then(errData => {
        throw new Error(JSON.stringify(errData));
      });
    }
    return response.json();
  });
}
