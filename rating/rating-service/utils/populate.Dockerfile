FROM postgres:14

# Install necessary packages and PostgreSQL client tools
RUN apt-get update && \
    apt-get install -y postgresql-client

WORKDIR /usr/src/app

FROM node:18-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install
RUN npm install dotenv
COPY . .
CMD ["node", "populate.mjs"]
