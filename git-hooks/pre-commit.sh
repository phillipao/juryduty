#!/bin/bash
# This hook is in SCM so that it can be shared.
# To install it, create a symbolic link in the project's .git/hooks folder.
#
#       i.e. - from the .git/hooks directory, run
#               $ ln -s ../../git-hooks/pre-commit.sh pre-commit
#
# to skip the tests, run with the --no-verify argument
#       i.e. - $ 'git commit --no-verify'
#
# Based on the pre-commit.sh gist at 
# https://gist.github.com/chadmaughan/5889802.

./gradlew test
exit $?
