#!/bin/bash

# Audio Quick Play - Build Helper for AndroidIDE
# Usage: bash build.sh [clean|debug|release|run]

set -e

PROJECT_NAME="Audio Quick Play"
APP_PACKAGE="com.example.audioquickplay"
BUILD_DIR="app/build"
APK_DEBUG="$BUILD_DIR/outputs/apk/debug/app-debug.apk"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${BLUE}╔════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║  $PROJECT_NAME${NC}"
    echo -e "${BLUE}║  Build Helper${NC}"
    echo -e "${BLUE}╚════════════════════════════════════╝${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

check_prerequisites() {
    echo -e "${BLUE}Checking prerequisites...${NC}"
    
    if ! command -v gradle &> /dev/null && [ ! -f "gradlew" ]; then
        print_error "Gradle not found!"
        echo "Install Gradle or use gradlew wrapper"
        exit 1
    fi
    
    if ! command -v java &> /dev/null; then
        print_error "Java not found!"
        echo "Install Java 11 or higher"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | awk -F'"' '/version/ {print $2}' | awk -F'.' '{print $1}')
    if [ "$JAVA_VERSION" -lt 11 ]; then
        print_error "Java version must be 11 or higher (found: $JAVA_VERSION)"
        exit 1
    fi
    
    print_success "Java $JAVA_VERSION found"
}

clean_build() {
    echo -e "${BLUE}Cleaning build...${NC}"
    if [ -f "gradlew" ]; then
        ./gradlew clean
    else
        gradle clean
    fi
    print_success "Build cleaned"
}

build_debug() {
    echo -e "${BLUE}Building debug APK...${NC}"
    if [ -f "gradlew" ]; then
        ./gradlew assembleDebug
    else
        gradle assembleDebug
    fi
    
    if [ -f "$APK_DEBUG" ]; then
        print_success "Debug APK built: $APK_DEBUG"
        ls -lh "$APK_DEBUG"
    else
        print_error "Debug APK not found!"
        exit 1
    fi
}

build_release() {
    echo -e "${BLUE}Building release APK...${NC}"
    print_warning "Release build requires signing!"
    print_warning "Update build.gradle with signing config first"
    
    if [ -f "gradlew" ]; then
        ./gradlew assembleRelease
    else
        gradle assembleRelease
    fi
}

install_adb() {
    echo -e "${BLUE}Installing via ADB...${NC}"
    
    if ! command -v adb &> /dev/null; then
        print_error "ADB not found! Make sure Android SDK tools are installed"
        exit 1
    fi
    
    DEVICES=$(adb devices | grep -v "^$" | grep -v "List of attached" | wc -l)
    
    if [ "$DEVICES" -eq 0 ]; then
        print_error "No device found! Connect a device with USB debugging enabled"
        exit 1
    fi
    
    print_success "Found $DEVICES device(s)"
    
    if [ -f "$APK_DEBUG" ]; then
        echo "Installing $APK_DEBUG..."
        adb install -r "$APK_DEBUG"
        print_success "App installed!"
    else
        print_error "$APK_DEBUG not found! Build first"
        exit 1
    fi
}

launch_app() {
    echo -e "${BLUE}Launching app...${NC}"
    
    if ! command -v adb &> /dev/null; then
        print_error "ADB not found!"
        exit 1
    fi
    
    adb shell am start -n "$APP_PACKAGE/.LoginActivity"
    print_success "App launched!"
}

show_help() {
    echo -e "${BLUE}Usage: bash build.sh [command]${NC}"
    echo ""
    echo "Commands:"
    echo "  clean       - Clean build directory"
    echo "  debug       - Build debug APK"
    echo "  release     - Build release APK (requires signing)"
    echo "  install     - Install debug APK via ADB"
    echo "  run         - Build, install, and launch app"
    echo "  help        - Show this help message"
    echo ""
    echo "Examples:"
    echo "  bash build.sh clean"
    echo "  bash build.sh debug"
    echo "  bash build.sh install"
    echo "  bash build.sh run"
}

main() {
    print_header
    
    COMMAND=${1:-help}
    
    case "$COMMAND" in
        clean)
            check_prerequisites
            clean_build
            ;;
        debug)
            check_prerequisites
            build_debug
            ;;
        release)
            check_prerequisites
            build_release
            ;;
        install)
            check_prerequisites
            build_debug
            install_adb
            ;;
        run)
            check_prerequisites
            clean_build
            build_debug
            install_adb
            launch_app
            ;;
        help)
            show_help
            ;;
        *)
            print_error "Unknown command: $COMMAND"
            show_help
            exit 1
            ;;
    esac
    
    echo ""
    print_header
}

main "$@"
