# money-transfer
Simple REST api for transfer between accounts

# Show users

Returns list of all users.

**URL** : `/user`

**Method** : `GET`

## Success response
**Code** : `200 OK`

**Content example**
```json
[
    {
        "id": 1,
        "name": "Test Name",
        "surname": "Test Surname",
        "accounts": [
            {
                "number": "1235 6789 0927 1839"
            }
        ]
    },
    {
        "id": 2,
        "name": "Test Name 2",
        "surname": "Test Surname 2",
        "accounts": [
            {
                "number": "8943 6723 1095 1928"
            }
        ]
    }
]
```

# Show user

Shows information about selected user

**URL** : `/user/:id/`

**URL Parameters** : `id=[integer]` where `id` is the ID of user to show.

**Method** : `GET`

## Success response

**Condition** : If user exists

**Code** : `200 OK`

**Content example**
```json
{
    "id": 1,
    "name": "Test Name",
    "surname": "Test Surname",
    "accounts": [
        {
            "number": "1235 6789 0927 1839"
        }
    ]
}
```

# Create user

Creates new user. If field `id` isn't provided a new ID will be automatically assigned.

**URL** : `/user`

**Method** : `POST`

**Data example**

```json
{
	"name": "Test Name 2",
	"surname": "Test Surname 2",
	"accounts": [
		{
			"number" : "8943 6723 1095 1928"
		}
	]
}
```

## Success response

**Condition** : If data is correct and user with the same ID don't exist.

**Code** : `200 OK`

## Error response

**Condition** : If user with provided ID already exists.

**Code** : `409 CONFLICT`

**Content**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 already exists"
}
```

# Update user

Updates fields for user with given ID.

**URL** : `/user/:id`

**URL Parameters** : `id=[integer]` where `id` is the ID of user.

**Method** : `PUT`

**Data example**

```json
{
	"name": "Test Name 2",
	"surname": "Test Surname 2",
	"accounts": [
		{
			"number" : "8943 6723 1095 1928"
		}
	]
}
```

## Success response

**Condition** : Data is correct and there is an user with given ID

**Code** : `200 OK`

## Error response

**Condition** : If user with provided ID don't exist.

**Code** : `409 CONFLICT`

**Content**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 doesn't exist"
}
```

# Delete all users

Deletes all users.

**URL** : `/user`

**Method** : `/delete`

## Success response

**Code** : `200 OK`

# Delete user

Deletes user with given ID.

**URL** : `/user/:id`

**URL Parameters** : `id=[integer]` where `id` is the ID of user.

**Mehod** : `DELETE`

## Success response

**Condition** : User with given ID exists.

**Code** : `200 OK`

## Error response

**Condition** : User with given ID doesn't exist

**Code** : `409 CONFLICT`

**Content**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 doesn't exist"
}
```

# Show users accounts
Shows users account list

**URL** : `/user/:id/account`

**URL Parameters** : `id=[integer]` where `id` is the ID of user.

**Method** : `GET`

## Success response

**Condition** : User with given ID exists.

**Code** : `200 OK`

## Error response

**Condition** : User with given ID doesn't exist.

**Code** : `409 CONFLICT`

**Content**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 doesn't exist"
}
```

# Show user account information
Shows information about user account

**URL** : `/user/:id/account/:number`

**URL Parameters** : `id=[integer], number=[string]` where `id` is the ID of user and `number` is an
account number.

**Method** : `GET`

## Success response

**Condition** : User with given ID exists.

**Code** : `200 OK`

**Content example**

```json
[
    {
        "number": "1235 6789 0927 1839"
    },
    {
        "number": "7890 7625 1873 1293"
    }
]
```

## Error response

**Condition** : User with given ID doesn't exist.

**Code** : `409 CONFLICT`

**Content**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 doesn't exist"
}
```

# Show account balance
Shows current account balance and transaction history.

**URL** : `/user/:id/account/:number/balance`

**Url parameters** : `id=[integer], number=[string]` where `id` is the ID of user and `number` is an account number

**Method** : `GET`

## Success response

**Condition** : User with given ID exists and has an account with given number.

**Code** : `200 OK`

**Content example**

```json
{
    "value": 184756,
    "history" : [
        {
            "id": 1,
            "inputNumber": "1234 8746 2837 9828",
            "outputNumber": "2859 0384 9285 6637",
            "value": 184756
        }
    ]
}
```

## Error response

**Condition** : User with given ID doesn't exist or does not have an account with given number.

**Code** : `409 CONFLICT`

**Content example**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 doesn't exist"
}
```

# Create account
Creates a new account for user

**URL** : `/user/:id/account`

**Url parameters** : `id=[integer], number=[string]` where `id` is the ID of user and `number` is an account number

**Method** : `POST`

**Data example** 

```json
{
    "number": "2317 4955 8876 2345"
}
```

## Success response

**Condition** : User with given ID exists and there is no account with given number.

**Code** : `200 OK`

## Error response

**Condition** : User with given ID exists or there is already an account with given number.

**Content**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 doesn't exist"
}
```

# Delete accounts
Deletes all account of user

**URL** : `/user/:id/account`

**Url parameters** : `id=[integer]` where `id` is the ID of user

**Method** : `DELETE`

## Success response

**Condition** : User with given ID exists

**Code** : `200 OK`

## Error response

**Condition** : User with given ID doesn't exist.

**Code** : `409 CONFLICT`

**Content**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 doesn't exist"
}
```

# Delete user's account
Deletes an account

**URL** : `/user/:id/account/:number`

**Url parameters** : `id=[integer], number=[string]` where `id` is the ID of user and `number` is
an account number

**Method** : `DELETE`

## Success response

**Condition** : User with given ID exists

**Code** : `200 OK`

## Error response

**Condition** : User with given ID doesn't exist.

**Code** : `409 CONFLICT`

**Content**

```json
{
    "status": "CONFLICT",
    "message": "User with id 1 doesn't exist"
}
```

# Get transfers
Returns list of all transfers between accounts

**URL** : `/transfer`

**Method** : `GET`

**Content example**

```json
[
    {
        "id" : 1,
        "inputNumber": "4573 4251 6889 0098",
        "outputNumber": "1948 7666 1931 0155",
        "value": 110
    }
]
```

## Success response

**Code** : `200 OK`

# Show transfer info
Shows transfer info

**URL** : `/transfer/:id`

**URL Parameters** : `id=[integer]` where `id` is the ID of a transfer.

**Method** : `GET`

**Content example**

```json
{
    "id" : 1,
    "inputNumber": "4573 4251 6889 0098",
    "outputNumber": "1948 7666 1931 0155",
    "value": 110
}
```

## Success response

**Code** : `200 OK`

# Add transfer
Transfers money from one account to another

**URL** : `/transfer`

**Method** : `POST`

**Data example**
```json
{
    "inputNumber": "4573 4251 6889 0098",
    "outputNumber": "1948 7666 1931 0155",
    "value": 110
}
```

## Success response

**Condition** : Both input and output account exists and there is no transfer with given ID.

**Code** : `200 OK`

## Error response

**Condition** : Input or output account doesn't exist or there is already a transfer with given ID.

**Code** : `409 CONFLICT`

**Content**

```json
{
    "status": "CONFLICT",
    "message": "Transfer with id 1 already exists"
}
```