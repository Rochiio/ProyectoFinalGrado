#FROM node:18.16.0-alpine AS build-step
#WORKDIR /app
#COPY package*.json ./
#RUN npm install --force
#COPY . .
#RUN npm run build
#EXPOSE 4200:80
#CMD ["npm", "start"]

#FROM nginx:1.20.1
#COPY --from=build-step /app/dist/frontend-whisker-watch /usr/share/nginx/html
#COPY ./ /usr/share/nginx/html
#EXPOSE 4200:80


FROM node:18.16.0-alpine
RUN mkdir -p /app
WORKDIR /app
COPY package*.json /app/
RUN npm install --force
COPY . /app/
EXPOSE 4200
CMD ["npm", "start"]
