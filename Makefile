run_all_tests:
	make -j java_tests python_tests dotnet_tests ruby_tests

dotnet_tests:
	echo "run dotnet tests"

java_tests:
	cd java && mvn install test -Dmaven.javadoc.skip=true;

python_tests:
	cd python && pip install -r requirements.txt && pytest;

ruby_tests:
	cd ruby && bundle install && bundle exec rake spec;
