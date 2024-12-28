FROM oven/bun

COPY bun.lockb . 
COPY package.json . 

RUN bun install

COPY . .

ENV POSTGRES_HOST=localhost
ENV POSTGRES_PORT=5438
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=azerty
ENV POSTGRES_DB=db_payment

EXPOSE 8085

CMD ["bun", "start"]
