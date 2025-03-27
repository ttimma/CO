
// ----------------- FLIGHTS (Fare Options and Flight Listing) -----------------

// HTML snippet for Economy BASIC fare details
const basicFareDetails = `
  <ul class="list-unstyled mt-3 mb-4">
    <li class="mb-2"><img src="./images/handbaggage.svg" height="30" style="margin-right: 8px;">1 cabin bag + 1 personal item (8 kg total)</li>
    <li class="mb-2"><img src="./images/rebooking.svg" height="30" style="margin-right: 8px;">Rebooking 50 € + possible fare difference</li>
    <li class="mb-2"><img src="./images/points.svg" height="30" style="margin-right: 8px;">1 point / €</li>
    <li class="mb-2" style="color: gray;"><img src="./images/nonrefundable.svg" height="30" style="margin-right: 8px; opacity: 0.6;">Non-refundable</li>
  </ul>
`;

// HTML snippet for Economy CLASSIC fare details
const classicFareDetails = `
  <ul class="list-unstyled mt-3 mb-4">
    <li class="mb-2"><img src="./images/handbaggage.svg" height="30" style="margin-right: 8px;">1 cabin bag + 1 personal item (8 kg total)</li>
    <li class="mb-2"><img src="./images/baggage.svg" height="30" style="margin-right: 8px;">1 bag (23 kg total)</li>
    <li class="mb-2"><img src="./images/seatselection.svg" height="30" style="margin-right: 8px;">Seat selection</li>
    <li class="mb-2"><img src="./images/rebooking.svg" height="30" style="margin-right: 8px;">Rebooking 50 € + possible fare difference</li>
    <li class="mb-2"><img src="./images/points.svg" height="30" style="margin-right: 8px;">1 point / € + 50 bonus points</li>
    <li class="mb-2" style="color: gray;"><img src="./images/nonrefundable.svg" height="30" style="margin-right: 8px; opacity: 0.6;">Non-refundable</li>
  </ul>
`;

// HTML snippet for Economy FLEX fare details
const flexFareDetails = `
  <ul class="list-unstyled mt-3 mb-4">
    <li class="mb-2"><img src="./images/handbaggage.svg" height="30" style="margin-right: 8px;">1 cabin bag + 1 personal item (8 kg total)</li>
    <li class="mb-2"><img src="./images/baggage.svg" height="30" style="margin-right: 8px;">2 bags (46 kg total)</li>
    <li class="mb-2"><img src="./images/seatselection.svg" height="30" style="margin-right: 8px;">Seat selection</li>
    <li class="mb-2"><img src="./images/priority.svg" height="30" style="margin-right: 8px;">Priority boarding</li>
    <li class="mb-2"><img src="./images/rebooking.svg" height="30" style="margin-right: 8px;">Rebooking before departure for free + possible fare differences</li>
    <li class="mb-2"><img src="./images/points.svg" height="30" style="margin-right: 8px;">1 point / € + 100 bonus points</li>
    <li class="mb-2"><img src="./images/refundable.svg" height="30" style="margin-right: 8px;">Fully refundable as a gift card or refund fee 25 €</li>
  </ul>
`;

/**
 * showFareOptions displays fare option cards (Basic, Classic, Flex) for the selected flight.
 * It calculates fare prices, builds the HTML content, inserts it into the DOM,
 * and attaches event listeners to the fare option buttons.
 * @param {Object} flight - The selected flight object.
 * @param {HTMLElement} flightRowEl - The table row element for the selected flight.
 */
export function showFareOptions(flight, flightRowEl) {
    // Calculate dynamic fare prices based on the base flight price.
    const basePrice = flight.price;
    const classicPrice = basePrice + 10000;
    const flexPrice = basePrice + 50000;
  
    // Build the HTML content for the fare options using the fare details constants.
    const fareHTML = `
      <div class="row row-cols-1 row-cols-md-3 mb-3 text-center">
        <div class="col">
          <div class="card mb-4 rounded-3 shadow-sm">
            <div class="card-header py-3">
              <h4 class="my-0 fw-normal">Economy BASIC</h4>
            </div>
            <div class="card-body">
              <h2 class="card-title pricing-card-title">${basePrice.toFixed(2)} €</h2>
              ${basicFareDetails}
              <button type="button" class="w-100 btn btn-lg btn-primary fare-option" data-fare="Basic" data-price="${basePrice}">
                Select
              </button>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card mb-4 rounded-3 shadow-sm">
            <div class="card-header py-3">
              <h4 class="my-0 fw-normal">Economy CLASSIC</h4>
            </div>
            <div class="card-body">
              <h2 class="card-title pricing-card-title">${classicPrice.toFixed(2)} €</h2>
              ${classicFareDetails}
              <button type="button" class="w-100 btn btn-lg btn-primary fare-option" data-fare="Classic" data-price="${classicPrice}">
                Select
              </button>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card mb-4 rounded-3 shadow-sm">
            <div class="card-header py-3">
              <h4 class="my-0 fw-normal">Economy FLEX</h4>
            </div>
            <div class="card-body">
              <h2 class="card-title pricing-card-title">${flexPrice.toFixed(2)} €</h2>
              ${flexFareDetails}
              <button type="button" class="w-100 btn btn-lg btn-primary fare-option" data-fare="Flex" data-price="${flexPrice}">
                Select
              </button>
            </div>
          </div>
        </div>
      </div>
    `;
  
    // Create a new table row to hold the fare options.
    const fareRow = document.createElement('tr');
    fareRow.id = `fare-row-${flight.flightNumber}`;
    // Create a cell that spans all columns.
    const fareCell = document.createElement('td');
    fareCell.colSpan = 9;
    fareCell.innerHTML = fareHTML;
    fareRow.appendChild(fareCell);
    // Insert the fare options row immediately after the flight row.
    flightRowEl.parentNode.insertBefore(fareRow, flightRowEl.nextSibling);
  
    // Attach event listeners to the fare option buttons so that when clicked,
    // the flight and fare details are stored and the user is redirected to the seat selection page.
    fareRow.querySelectorAll('.fare-option').forEach(btn => {
      btn.addEventListener('click', e => {
        const selectedFare = e.target.getAttribute('data-fare');
        const selectedPrice = e.target.getAttribute('data-price');
        sessionStorage.setItem('selectedFlightFare', JSON.stringify({
          flight: flight,
          fare: selectedFare,
          price: selectedPrice
        }));
        // Redirect the user to the seat map page.
        window.location.href = 'seats.html';
      });
    });
  }

  /**
 * renderRoutes renders routes into a given container element.
 * @param {Array} routes - Array of route objects.
 * @param {HTMLElement} container - The container element (e.g., tbody) to render rows.
 */
export function renderRoutes(routes, container) {
  container.innerHTML = '';
  routes.forEach(route => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${route.flightNumber}</td>
      <td>${route.from}</td>
      <td>${route.to}</td>
      <td>${route.distance}</td>
      <td>${route.price}</td>
      <td>${route.flightStart}</td>
      <td>${route.flightEnd}</td>
      <td>${route.companyName}</td>
      <td><button class="btn btn-primary select-flight">Select</button></td>
    `;
    row.querySelector('.select-flight').addEventListener('click', () => {
      // Build object with necessary flight data including route and PriceList IDs.
      const selectedFlightData = {
        id: route.id,
        priceListId: route.priceListId,
        flightNumber: route.flightNumber,
        from: route.from,
        to: route.to,
        price: route.price,
        companyName: route.companyName
      };
      const selectedFare = "Classic"; // default fare option
      sessionStorage.setItem('selectedFlightFare', JSON.stringify({
        flight: selectedFlightData,
        fare: selectedFare,
        price: route.price
      }));
      // Call fare options UI function.
      showFareOptions(route, row);
    });
    container.appendChild(row);
  });
}

  // ----------------- SEATS (Seat Map Rendering and Selection) -----------------

/**
 * renderSeats renders the seat map into the page.
 * It positions each seat based on predefined spacing constants and attaches click event listeners.
 * @param {Array} seats - Array of seat objects.
 * @param {Function} toggleSeatSelection - Callback function to handle seat click events.
 */
export function renderSeats(seats, toggleSeatSelection) {
    console.log("renderSeats called with:", seats); // Debug log to verify seats data.
    const seatMap = document.getElementById('seat-map');
    seatMap.innerHTML = ''; // Clear any previous seat map content.
    
    const seatLabels = ['A', 'B', 'C', 'D', 'E', 'F'];
    
    // Define constants for seat positioning on the map.
    const seatHorizontalSpacing = 16;
    const seatVerticalSpacing = 23;
    const leftOffset = 99;
    const topOffset = 202;
    const aisleWidth = 22;
    const extraLegroomSpacing = 8;
    
    let currentRowNumber = null;
    let currentRowOffset = topOffset;
    let currentRowElement = null;
    
    // Loop through each seat and create its DOM element.
    seats.forEach(seat => {
      // When a new row is encountered, update the current row and offset.
      if (seat.row !== currentRowNumber) {
        currentRowNumber = seat.row;
        currentRowOffset += seatVerticalSpacing;
        // Add extra spacing for specific rows (e.g., rows 11 or 12).
        if (seat.row === 11 || seat.row === 12) {
          currentRowOffset += extraLegroomSpacing;
        }
        currentRowElement = document.createElement('div');
        currentRowElement.classList.add('row');
        currentRowElement.dataset.row = seat.row;
        seatMap.appendChild(currentRowElement);
      }
    
      // Calculate the left offset for the seat based on its column.
      let seatLeftOffset = leftOffset + (seat.column - 1) * seatHorizontalSpacing;
      if (seat.column >= 4) {
        seatLeftOffset += aisleWidth;
      }
      const seatTopOffset = currentRowOffset;
    
      // Create the seat element and set its styling and ID.
      const seatElement = document.createElement('div');
      seatElement.classList.add('plane-seat');
      seatElement.id = `seat-${seat.row}-${seat.column}`;
      seatElement.style.left = `${seatLeftOffset}px`;
      seatElement.style.top = `${seatTopOffset}px`;
    
      // Set the seat label (letter corresponding to the seat column).
      seatElement.textContent = seatLabels[seat.column - 1];
    
      // Add a class to indicate whether the seat is taken or available.
      if (seat.taken) {
        seatElement.classList.add('taken');
      } else {
        seatElement.classList.add('available');
        // If the seat is available, attach an event listener to toggle selection on click.
        seatElement.addEventListener('click', () => toggleSeatSelection(seat));
      }
    
      // Append the seat element to the current row element.
      currentRowElement.appendChild(seatElement);
    });
  }
  
  /**
   * displayChosenSeats updates the UI to list the seats selected by the user.
   * It populates an unordered list with the seat labels.
   * @param {Array} seatsArray - Array of seat objects that have been selected.
   */
  export function displayChosenSeats(seatsArray) {
    const chosenSeatsDiv = document.getElementById('chosen-seats');
    const chosenSeatsList = document.getElementById('chosen-seats-list');
    
    // If the required elements are missing, exit the function.
    if (!chosenSeatsDiv || !chosenSeatsList) return;
    
    // Clear any previously displayed chosen seats.
    chosenSeatsList.innerHTML = '';
    
    if (seatsArray.length === 0) {
      // If no seats are selected, hide the chosen seats section.
      chosenSeatsDiv.style.display = 'none';
      return;
    }
    
    // Display the chosen seats section.
    chosenSeatsDiv.style.display = 'block';
    const seatLabels = ['A', 'B', 'C', 'D', 'E', 'F'];
    
    // Loop through each selected seat and add it to the list.
    seatsArray.forEach((seat, index) => {
      const colLabel = seatLabels[seat.column - 1];
      const seatLabel = `${seat.row}${colLabel}`;
      const listItem = document.createElement('li');
      listItem.textContent = `Passenger ${index + 1}: ${seatLabel}`;
      chosenSeatsList.appendChild(listItem);
    });
  }

  // ---------- PASSENGER UI ---------//

// passengerUi.js

/**
 * Renders passenger input fields for each chosen seat.
 * @param {Array} chosenSeats - Array of chosen seat objects.
 * @param {string} containerId - The id of the container element.
 */
export function renderPassengerFields(chosenSeats, containerId) {
  const container = document.getElementById(containerId);
  container.innerHTML = ''; // Clear previous content

  if (chosenSeats.length === 0) {
    container.innerHTML = '<p>No seats selected. Please select seats before entering passenger information.</p>';
    return;
  }

  const seatLabels = ['A', 'B', 'C', 'D', 'E', 'F'];
  chosenSeats.forEach((seat, index) => {
    const seatLabel = `${seat.row}${seatLabels[seat.column - 1]}`;
    const fieldset = document.createElement('fieldset');
    fieldset.classList.add('mb-3');

    const legend = document.createElement('legend');
    legend.textContent = `Passenger ${index + 1} (Seat: ${seatLabel})`;
    fieldset.appendChild(legend);

    // First Name field
    const firstNameDiv = document.createElement('div');
    firstNameDiv.classList.add('mb-2');
    const firstNameLabel = document.createElement('label');
    firstNameLabel.setAttribute('for', `firstName${index}`);
    firstNameLabel.textContent = 'First Name:';
    firstNameDiv.appendChild(firstNameLabel);
    const firstNameInput = document.createElement('input');
    firstNameInput.type = 'text';
    firstNameInput.classList.add('form-control');
    firstNameInput.id = `firstName${index}`;
    firstNameInput.required = true;
    firstNameDiv.appendChild(firstNameInput);
    fieldset.appendChild(firstNameDiv);

    // Last Name field
    const lastNameDiv = document.createElement('div');
    lastNameDiv.classList.add('mb-2');
    const lastNameLabel = document.createElement('label');
    lastNameLabel.setAttribute('for', `lastName${index}`);
    lastNameLabel.textContent = 'Last Name:';
    lastNameDiv.appendChild(lastNameLabel);
    const lastNameInput = document.createElement('input');
    lastNameInput.type = 'text';
    lastNameInput.classList.add('form-control');
    lastNameInput.id = `lastName${index}`;
    lastNameInput.required = true;
    lastNameDiv.appendChild(lastNameInput);
    fieldset.appendChild(lastNameDiv);

    container.appendChild(fieldset);
  });
}

/**
 * Reads passenger data from the rendered form fields.
 * @param {number} count - The number of passengers (fields) to read.
 * @returns {Array} - Array of passenger objects with firstName and lastName.
 */
export function getPassengersFromForm(count) {
  const passengers = [];
  for (let i = 0; i < count; i++) {
    const firstName = document.getElementById(`firstName${i}`).value.trim();
    const lastName = document.getElementById(`lastName${i}`).value.trim();
    if (!firstName || !lastName) {
      throw new Error(`Please fill in all fields for passenger ${i + 1}`);
    }
    passengers.push({ firstName, lastName });
  }
  return passengers;
}
