mkdir RRS;
cd RRS;
svn2git https://svn.mpi.nl/LAT --authors ../users.txt --tag rrs/tags --branches rrs/branches --trunk rrs/trunk;
git remote add origin https://github.com/TheLanguageArchive/RRS.git;
git push --all -u;
git push --tags
cd ..;