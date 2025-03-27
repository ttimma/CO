import { showFareOptions, renderRoutes } from './ui.js';
import { fetchOrigins, fetchDestinations, fetchCompanies, fetchRoutes, updatePriceList } from './api.js';

// Populate dropdowns
function populateSelect(endpoint, selectId, defaultText) {
  fetch(endpoint)
    .then(response => response.json())
    .then(data => {
      const selectElement = document.getElementById(selectId);
      selectElement.innerHTML = `<option value="">${defaultText || 'Select an option'}</option>`;
      data.forEach(item => {
        const option = document.createElement('option');
        option.value = item;
        option.textContent = item;
        selectElement.appendChild(option);
      });
    })
    .catch(error => console.error(`Error populating ${selectId}: ${error.message}`));
}

document.addEventListener("DOMContentLoaded", function() {
  // Force PriceList update, then populate dropdowns
  updatePriceList()
    .then(updatedPriceList => {
      console.log("Latest PriceList updated:", updatedPriceList);
      populateSelect("http://localhost:8080/api/origins", "origin", "Select Origin");
      populateSelect("http://localhost:8080/api/destinations", "destination", "Select Destination");
      populateSelect("http://localhost:8080/api/companies", "companyName", "Select Company (optional)");
    })
    .catch(error => {
      console.error(`Error during PriceList update: ${error.message}`);
      populateSelect("http://localhost:8080/api/origins", "origin", "Select Origin");
      populateSelect("http://localhost:8080/api/destinations", "destination", "Select Destination");
      populateSelect("http://localhost:8080/api/companies", "companyName", "Select Company (optional)");
    });
  
  // Add event listener to update destinations based on selected origin
  document.getElementById('origin').addEventListener('change', function(e) {
    const selectedOrigin = e.target.value.trim();
    let endpoint = "http://localhost:8080/api/destinations";
    if (selectedOrigin) {
      endpoint += `?origin=${encodeURIComponent(selectedOrigin)}`;
    }
    populateSelect(endpoint, "destination", "Select Destination");
  });
});

// Route search logic
const routeSearchForm = document.getElementById('routeSearchForm');
const routesError = document.getElementById('routesError');
const routesTable = document.getElementById('routesTable');
const routesTableBody = document.getElementById('routesTableBody');

routeSearchForm.addEventListener('submit', (event) => {
  event.preventDefault();
  routesError.textContent = '';
  routesTableBody.innerHTML = '';
  routesTable.style.display = 'none';

  const origin = document.getElementById('origin').value.trim();
  const destination = document.getElementById('destination').value.trim();
  const companyName = document.getElementById('companyName').value.trim();
  const sortBy = document.getElementById('sortBy').value.trim();

  let queryParams = `?origin=${encodeURIComponent(origin)}&destination=${encodeURIComponent(destination)}`;
  if (companyName) queryParams += `&companyName=${encodeURIComponent(companyName)}`;
  if (sortBy) queryParams += `&sortBy=${encodeURIComponent(sortBy)}`;

  fetchRoutes(queryParams)
    .then(data => {
      if (data.length === 0) {
        routesError.textContent = 'No routes found.';
        return;
      }
      routesTable.style.display = 'table';
      renderRoutes(data, routesTableBody);
    })
    .catch(err => {
      routesError.textContent = `Error: ${err.message}`;
    });
});
