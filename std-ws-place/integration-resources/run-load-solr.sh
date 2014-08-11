#!/bin/bash

# -------------------------------------------------------------------------------------------------
# set-up some environment variables, etc.  NOTE: please edit the "solrhome" and "flatfile"
# paths to the correct directory locations.
# -------------------------------------------------------------------------------------------------

solrhome=/home/ubuntu/.place
flatfile=/home/ubuntu/flat-file
jargs="-Xms2000m -Xmx2000m -Xss48m"
pargs="--dbHost localhost --dbSchema p124 --dbUser sams_place --dbPassword sams_place --solrHome ${solrhome} --baseDir ${flatfile}"

# -------------------------------------------------------------------------------------------------
# create the class path.  NOTE: please edit the path to where the JAR files actually reside
# -------------------------------------------------------------------------------------------------
cp=.
for ff in /home/ubuntu/test-env/load-solr/lib/*.jar  # edit this line!!
do
  cp=${cp}:${ff}
done


#echo java -cp ${cp} ${jargs} org.familysearch.standards.place.solr.load.LoadSolrApp ${pargs}
java -cp ${cp} ${jargs} org.familysearch.standards.place.solr.load.LoadSolrApp ${pargs}
~
~
