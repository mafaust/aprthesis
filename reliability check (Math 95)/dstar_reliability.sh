VAR_METRIC="dstar"
java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/1/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/1/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/2/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/2/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/3/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/3/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/4/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/4/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/5/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/5/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/6/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/6/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/7/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/7/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/8/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/8/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/9/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/9/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  \
; java -cp target/astor-*-jar-with-dependencies.jar fr.inria.main.evolution.AstorMain  -location /Users/marekmazur/astor/defects4j/math-95 -mode jgenprog -package org.apache.commons -jvm4testexecution /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin -failing org.apache.commons.math.distribution.FDistributionTest -srcjavafolder /src/java/ -srctestfolder /src/test/ -binjavafolder /target/classes/ -bintestfolder /target/test-classes/ -stopfirst true -dependencies /Users/marekmazur/astor/examples/libs/junit-4.4.jar -maxgen 1000000 -seed 10 -maxtime 60 -scope local -stopfirst true -flthreshold 0 -population 1 -faultlocalization flacoco | tee /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/10/log.txt \
; cp -R /Users/marekmazur/astor/output_astor/AstorMain-math-95/ /Users/marekmazur/Desktop/Results/reliability_check/$VAR_METRIC/10/ \
; kill -9 $(ps -ef | grep "java" | grep -v grep | awk '{print $2}')  

