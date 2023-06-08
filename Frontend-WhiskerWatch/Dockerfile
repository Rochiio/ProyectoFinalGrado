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

# Create image based on the official Node 10 image from dockerhub
FROM node:18.16.0-alpine

# Create a directory where our app will be placed
RUN mkdir -p /app

# Change directory so that our commands run inside this new directory
WORKDIR /app

# Copy dependency definitions
COPY package*.json /app/

# Install dependecies
RUN npm install --force

# Get all the code needed to run the app
COPY . /app/

# Expose the port the app runs in
EXPOSE 4200

# Serve the app
CMD ["npm", "start"]
