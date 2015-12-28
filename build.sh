lein cljsbuild once 
pushd ./resources/public/
python2 -m SimpleHTTPServer $PORT

