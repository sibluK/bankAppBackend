import React, { useState, useEffect } from 'react';

function Student() {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        fetch(`http://localhost:7171/users`)
            .then(res => res.json())
            .then(data => {
                console.log('Fetched users:', data);
                setUsers(data._embedded.userModelList);
            })
            .catch(error => {
                console.error('Error fetching users:', error);
                setUsers([]); // Set users to an empty array to clear any previous data
            });
    }, []);


    return (
        <div>
            <h1>Users</h1>
            <ul>
                {Array.isArray(users) ? (
                    users.map(user => (
                        <li key={user.id}>
                            {user.firstName}
                            {user.lastName}
                        </li>
                    ))
                ) : (
                    <li>No users found</li>
                )}
            </ul>

        </div>
    );
}

export default Student;