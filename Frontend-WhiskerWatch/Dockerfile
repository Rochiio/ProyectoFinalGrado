# Use an official Node.js runtime as a parent image
FROM node:20-alpine

# Set the working directory to /app
WORKDIR /app

# Copy package.json and package-lock.json to /app
COPY package*.json ./

# Install app dependencies
RUN npm install --force

# Copy app source code to /app
COPY . .

# Build the app for production
RUN npm run build

# Expose port 80 for the container
EXPOSE 4200

# Start the app
CMD ["npm", "start"]
