#!/bin/bash

set -e -x

git init
git add Sources
git add .gitignore
git add LICENSE
git add Package.swift
git add README.md
git branch -M local
TIMESTAMP="$(date '+%Y-%m-%d %T')"
TAG="patch(chat-ui-ios):"
git commit -m "$TAG git init $TIMESTAMP"
git remote add origin git@github.com:ChatKitty/chat-ui-ios.git
git fetch
git checkout main
git merge local --allow-unrelated-histories --strategy-option theirs -m "$TAG release $TIMESTAMP"
git push -u origin main
rm -rf .git
