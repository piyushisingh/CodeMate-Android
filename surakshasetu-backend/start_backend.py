#!/usr/bin/env python3
"""
Startup script for the Node.js backend
"""

import subprocess
import sys
import os
import time
import requests
import socket

def check_mongodb():
    """Check if MongoDB is running by trying to connect to the default port"""
    try:
        # Try to connect to default MongoDB port
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.settimeout(2)
        result = sock.connect_ex(('127.0.0.1', 27017))
        sock.close()
        
        if result == 0:
            print("✅ MongoDB is running")
            return True
        else:
            print("❌ MongoDB is not accessible on port 27017")
            return False
    except Exception as e:
        print(f"❌ Error checking MongoDB: {e}")
        return False

def start_backend():
    """Start the Node.js backend"""
    try:
        print("🚀 Starting Node.js backend...")
        
        # Change to backend directory
        backend_dir = os.path.join(os.path.dirname(__file__))
        os.chdir(backend_dir)
        
        # Start the backend in development mode
        process = subprocess.Popen(
            ["npm", "run", "dev"],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        
        print("⏳ Waiting for backend to start...")
        
        # Wait a bit for the server to start
        time.sleep(5)
        
        # Check if the server is responding
        try:
            response = requests.get("http://localhost:5000", timeout=5)
            if response.status_code == 200:
                print("✅ Backend is running successfully!")
                print("📄 API Documentation: http://localhost:5000")
                return process
            else:
                print(f"⚠️  Backend responded with status: {response.status_code}")
        except Exception as e:
            print(f"❌ Could not connect to backend: {e}")
            
        return process
        
    except Exception as e:
        print(f"❌ Failed to start backend: {e}")
        return None

def main():
    print("🔧 CodeMate Node.js Backend Startup Script")
    print("=" * 45)
    
    # Check prerequisites
    print("🔍 Checking prerequisites...")
    
    # Check if Node.js is installed
    try:
        result = subprocess.run(["node", "--version"], capture_output=True, text=True)
        print(f"✅ Node.js {result.stdout.strip()} found")
    except FileNotFoundError:
        print("❌ Node.js not found. Please install Node.js first.")
        return
    
    # Check if npm is installed
    try:
        result = subprocess.run(["npm", "--version"], capture_output=True, text=True)
        print(f"✅ npm {result.stdout.strip()} found")
    except FileNotFoundError:
        print("❌ npm not found. Please install Node.js (which includes npm) first.")
        return
    
    # Check MongoDB
    if not check_mongodb():
        print("💡 Please start MongoDB before running the backend.")
        print("   On Windows: net start MongoDB")
        print("   On macOS/Linux: sudo systemctl start mongod")
        return
    
    # Install dependencies if needed
    print("📦 Installing dependencies...")
    try:
        subprocess.run(["npm", "install"], check=True, capture_output=True)
        print("✅ Dependencies installed")
    except subprocess.CalledProcessError as e:
        print("⚠️  Error installing dependencies:")
        print(e.stderr.decode() if e.stderr else "Unknown error")
    
    # Start the backend
    process = start_backend()
    
    if process:
        print("\n🔄 Backend is running in the background.")
        print("   Press Ctrl+C to stop the backend.")
        print("   API endpoints are available at http://localhost:5000")
        
        try:
            # Keep the script running
            while True:
                time.sleep(1)
        except KeyboardInterrupt:
            print("\n🛑 Stopping backend...")
            process.terminate()
            print("✅ Backend stopped.")
    else:
        print("❌ Failed to start backend. Check the error messages above.")

if __name__ == "__main__":
    main()