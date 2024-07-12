# Use the official Ubuntu base image
FROM ubuntu:20.04

# Install required packages
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget unzip git && \
    apt-get clean

# Set environment variables for Android SDK
ENV ANDROID_SDK_ROOT=/sdk
ENV PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

# Download and install Android SDK
RUN mkdir -p $ANDROID_SDK_ROOT && \
    cd $ANDROID_SDK_ROOT && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-7583922_latest.zip -O cmdline-tools.zip && \
    unzip cmdline-tools.zip && \
    mkdir -p cmdline-tools/latest && \
    mv cmdline-tools/* cmdline-tools/latest && \
    rm cmdline-tools.zip
# Accept Android SDK licenses
RUN yes | sdkmanager --licenses

# Install required Android SDK packages
RUN sdkmanager "platform-tools" "platforms;android-30" "build-tools;30.0.3"

# Copy the project files into the container
COPY . /app

# Set the working directory to /app
WORKDIR /app

# Build the Android project
RUN ./gradlew build

# Expose port 8080 (if your app needs it)
EXPOSE 8080

# Command to run the application
CMD ["./gradlew", "assembleDebug"]
