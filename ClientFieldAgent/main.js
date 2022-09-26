let allAgents = [];
let currentAgentId = 0;

function init() {
  document
    .getElementById('addAgent')
    .addEventListener('submit', handleSubmit);
  displayList();
}

function setView(view) {
  switch (view) {
    case 'form':
      document.getElementById('agentList').style.display = 'none';
      document.getElementById('addAgent').style.display = 'block';
      document.getElementById('confirmDelete').style.display = 'none';

      break;
    case 'table':
      document.getElementById('agentList').style.display = 'block';
      document.getElementById('addAgent').style.display = 'none';
      document.getElementById('confirmDelete').style.display = 'none';

      break;
    case 'delete':
      document.getElementById('agentList').style.display = 'none';
      document.getElementById('addAgent').style.display = 'none';
      document.getElementById('confirmDelete').style.display = 'block';
  }
}
async function displayList() {
  const response = await fetch('http://localhost:8080/api/agent');
  if (response.status === 200) {
    allAgents = await response.json();
    renderAgents();
    setView('table');
  }
}

function renderAgents() {
  const agentListContainer = document.getElementById('agentTable');
  let html = `<table class="table table-warning">
        <thead class="table-dark">
          <tr>
            <th>ID # </th>
            <th>First Name</th>
            <th>Middle-Last Name</th>
            <th>Date of Birth</th>
            <th>Height</th>
            <th>Functions</th>
          </tr>
        </thead> <tbody class="table-group-divider">`;
  for (let agent of allAgents) {
    html += ` 
          <tr>
            <td>${agent.agentId}</td>
            <td>${agent.firstName}</td>
            <td>${agent.middleName}-${agent.lastName}</td>
            <td>${agent.dob}</td>
            <td>${agent.heightInInches}</td> 
            <td>
              <button type="button" onclick="handleEditAgent(${agent.agentId})" class="btn btn-primary btn-sm">Edit</button>
            </td>
            <td>
              <button type="button" onclick="handleDeleteAgent(${agent.agentId})" class="btn btn-danger btn-sm">
                Delete
              </button>
            </td>
          </tr>`;
  }

  html += `</tbody></table>`;

  agentListContainer.innerHTML = html;
}

async function handleSubmit(evt) {
  evt.preventDefault();

  document.getElementById('errors').style.display = 'none';

  const agent = {
    agentId: currentAgentId,
    firstName: document.getElementById('firstName').value,
    middleName: document.getElementById('middleName').value,
    lastName: document.getElementById('lastName').value,
    dob: document.getElementById('dob').value,
    heightInInches: document.getElementById('heightInInches').value,
  };

  const init = {
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
    },
    body: JSON.stringify(agent),
  };

  if (currentAgentId > 0) {
    // update
    init.method = 'PUT';

    const response = await fetch(
      `http://localhost:8080/api/agent/${agent.agentId}`,
      init
    );
    if (response.status === 204) {
      displayList();
    } else if (response.status === 400) {
      const errors = await response.json();
      displayErrors(errors);
    } else if (response.status === 404) {
      displayErrors(['agent not found.']);
    } else {
      console.error('something unexpected happened.');
    }
  } else {
    // add
    init.method = 'POST';

    const response = await fetch('http://localhost:8080/api/agent', init);
    if (response.status === 201) {
      displayList();
    } else if (response.status === 400) {
      const errors = await response.json();
      displayErrors(errors);
    } else {
      console.error('something unexpected happened.');
    }
  }
}

function handleAddAgent() {
  currentAgentId = 0;

    document.getElementById('firstName').value = '';
    document.getElementById('middleName').value = '';
    document.getElementById('lastName').value = '';
    document.getElementById('dob').value = '';
    document.getElementById('heightInInches').value = 0;

  setView('form');
}

function handleEditAgent(agentId) {
  currentAgentId = agentId;
  const agent = allAgents.find((agent) => agent.agentId === agentId);

    document.getElementById('firstName').value = agent.firstName;
    document.getElementById('middleName').value = agent.middleName;
    document.getElementById('lastName').value = agent.lastName;
    document.getElementById('dob').value = agent.dob;
    document.getElementById('heightInInches').value = agent.heightInInches;

  setView('form');
}

function handleDeleteAgent(agentId) {
  currentAgentId = agentId;
  const agent = allAgents.find((agent) => agent.agentId === agentId);

  document.getElementById('firstNameLabel').innerHTML = agent.firstName;
  document.getElementById('middleNameLabel').innerHTML = agent.middleName;
  document.getElementById('lastNameLabel').innerHTML = agent.lastName;
  document.getElementById('dobLabel').innerHTML = agent.dob;
  document.getElementById('heightInInchesLabel').innerHTML = agent.heightInInches;

  setView('delete');
}

async function handleConfirmDelete() {
  const response = await fetch(
    `http://localhost:8080/api/agent/${currentAgentId}`,
    { method: 'DELETE' }
  );
  if (response.status === 204) {
    displayList();
  } else if (response.status === 404) {
    displayErrors(['agent not found.']);
  } else {
    console.error('something unexpected happened.');
  }
  setView('table');
}

function displayErrors(errors) {
  if (errors) {
    let html = '<ul>';
    for (let err of errors) {
      html += `<li>${err}</li>`;
    }
    html += '</ul>';
    document.getElementById('errors').innerHTML = html;
    document.getElementById('errors').style.display = 'block';
  }
} 

init();