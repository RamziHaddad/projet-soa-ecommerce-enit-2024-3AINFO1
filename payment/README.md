# payment

## Some notes
This API has all CRUD functionalities in addition to the ability to get a single row based on the iduser.
bun can be replaced with different runtimes such as npm.

## To install dependencies:

```bash
bun install
```

To run:

```bash
bun start
```
## Postgres

Open up a terminal in the project directory and run:

```bash
docker-compose up
```
### To recreate the DB

```bash
psql -h localhost -p 5438 -U postgres -d db_payment
```
You will be prompted for the password `azerty`

The code used to create the PosgreSQL database.
```SQL
CREATE TABLE cartes_bancaires (
    idUser SERIAL PRIMARY KEY,
    codeCarte VARCHAR(16) NOT NULL UNIQUE,
    CodeSecret VARCHAR(4) NOT NULL,
    CHECK (codeCarte ~ '^[0-9]+$'),
    CHECK (CodeSecret ~ '^[0-9]+$')
);
```
The card code must be unique and it was made to be VARCHAR with length of 16 characters with a constraint of it being all integers instead of making it an integer type so that the leading zeros wouldn't be omitted, same for the secret code.

## Testings

P.S. I will be adding testing images soon to all the CRUD functionalities.

![Database structure](/assets/DB.png "DB")
![Get test with curl](/assets/test.png "cURL Test")

