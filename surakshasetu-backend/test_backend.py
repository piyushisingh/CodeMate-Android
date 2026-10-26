#!/usr/bin/env python3
"""
Simple test script to verify the Node.js backend is working
"""

import requests
import json

def test_backend():
    base_url = "http://localhost:5000"
    
    # Test basic endpoint
    try:
        response = requests.get(base_url)
        print(f"Basic endpoint test: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"Error testing basic endpoint: {e}")
        return
    
    # Test API endpoint
    try:
        response = requests.get(f"{base_url}/api/videos")
        print(f"API videos endpoint test: {response.status_code}")
        if response.status_code == 200:
            print(f"  Response: {response.json()}")
    except Exception as e:
        print(f"Error testing API videos endpoint: {e}")
    
    # Test check endpoint
    try:
        response = requests.get(f"{base_url}/api/videos/check/python/variables")
        print(f"API check endpoint test: {response.status_code}")
        if response.status_code == 200:
            print(f"  Response: {response.json()}")
    except Exception as e:
        print(f"Error testing API check endpoint: {e}")

if __name__ == "__main__":
    print("Testing Node.js Backend...")
    test_backend()