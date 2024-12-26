FROM oven/bun

COPY bun.lockb . 
COPY package.json . 

RUN bun install

COPY . .

EXPOSE 8085

CMD ["bun", "start"]
