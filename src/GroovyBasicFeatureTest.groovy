/**
 * Test some basic language features
 * works with groovy-2.1.7 or better
 *
 * Created with IntelliJ IDEA.
 * User: armin
 * Date: 11.10.13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */

import org.junit.*
import static org.junit.Assert.*

class GroovyBasicFeatureTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        println "Groovy Version: " + GroovySystem.getVersion()
        // println "Groovy Version ${org.codehaus.groovy.runtime.InvokerHelper.version}"
    }

    @Test
    void testAssertions() {

        assertTrue(true)
        assertTrue("comment", true)
        assertTrue true
        assertTrue(1 == 1)
        assertTrue 1 == 1
        assertTrue "comment", 1 == 1

        assertFalse(false)
        assertFalse(1 == 2)
        assertFalse(1L == 2L)

        assertEquals 'shit happens', 0, 0
        assertEquals 0, 0
        assertEquals 'a', "a"
    }

    @Test
    void testList() {
        List<String> list = ['AA','BB','CC']
        assert list[0] == 'AA'
        assert list.contains('BB')
        assert list.containsAll(['AA','BB'])
        assert list.containsAll(['BB','AA']) // sequence doesn't matter
        assert ! list.containsAll(['AA','BB', 'notthere'])
        assert list.containsAll(['CC','AA','BB'])
        assert list.containsAll('AA')
        assert list.size == 3
        assert list[0].class.name == 'java.lang.String'
        list << 'EE'
        assert list.size == 4
        assert list[3] == 'EE'
        assert list == ['AA','BB','CC','EE']
    }

    @Test(expected=NullPointerException.class)
    void testIgnoreNPE() {
        throw new NullPointerException()
    }

    @Test(expected=NullPointerException.class)
    void testIgnoreNPE2() {
        String s = null
        s.size()
    }

    @Test(expected=NullPointerException.class)
    void testIgnoreNPE3() {
        String s // initialized with null
        s.size()
    }

    @Test
    void testSafeNavigationOperator() {
        String s = null
        //Safe Navigation Operator, safe dereference operator (?.)
        Integer size = s?.size()
        assert size == null
        assert null?.capitalize() == null
    }

    @Test
    void testSafeNavigationOperator2() {
        File file = null
        assert file?.name == null
        assert file?.name?.bytes?.length() == null
    }

    @Test
    void testSafeNavigationOperator3() {
        def something
        assert something?.notThere == null
    }

    @Test
    void testElvisOperator() {
        // Elvis Operator (?:)
        def val = null
        def result

        result = val != null ? val : 'x'
        assert result == 'x'
        result = val ?: 'x'
        assert result == 'x'

        val = 'hello'
        result = val != null ? val : 'x'
        assert result == 'hello'
        result = val ?: 'x'
        assert result == 'hello'
    }

    @Test
    void testCatchAll() {
        def msg
        try {
            throw new NullPointerException('shit')
        } catch (all) { // similar to catch (Throwable all)
            msg = all.message
        }
        assert 'shit' == msg
    }

    @Test
    void testCatchAll2() {
        def msg
        try {
            throw new NullPointerException('bullshit')
        } catch (shithappens) { // similar to catch (Throwable all)
            msg = shithappens.message
        }
        assert 'bullshit' == msg
    }

    @Test
    void testMultiplyOperatorOnStrings() {
        assert 'aaa' == 'a'*3
        assert 'a1a1' == 'a1'*2
    }

    @Test
    void testSpaceshipOperator() {
        // Spaceship Operator (<=>)
        assert (0 <=> 1) < 0
        assert (1 <=> 0) > 0
        assert (2 <=> 2) == 0
        assert (2 <=> 2) >= 0
        assert ('a' <=> 'b') < 0
        assert ('a' <=> 'aaa') < 0
        assert ('a' <=> ('a'*3)) < 0
        assert ('1' <=> 'a') < 0
        assert ('A' <=> 'a') < 0
        assert ('a1' <=> 'aa') < 0
    }

    @Test
    void testSpreadOperator() {
        //Spread Operator (*.)
        def animals = ['cat', 'elephant']
        assert animals*.size() == [3, 8]
        assert animals*.size() ==  animals.collect { it?.size() }
    }

    @Test
    void testCollect() {
        // return square
        assert [1, 2, 3, 4].collect { it*it } == [1, 4 ,9, 16]
        assert (1..4).collect { it*it } == [1, 4 ,9, 16]
    }

    @Test
    void testMinMaxOnCollection() {
        assert 5 == [5, 5, 1].max()
        assert 1 == [5, 5, 1].min()
        assert 3 == (1..<4).max()
        assert 'z' == ('a'..'z').max()
        assert 'aaa' == ['a','aa','aaa'].max()
        assert 'b' == ['a','b','aa','aaa'].max()
    }

    // groovy doesn't check list constructors (yet?)
    @Test(expected=AssertionError.class)
    void testListMixedTypes() {
        List<String> list = [123, 'AA']
        assert list[0].class.name == 'java.lang.String'
    }

    // groovy doesn't check list constructors yet
    @Test
    void testListMixedTypes2() {
        List<String> list = [123, 'AA']
        assert list[0] instanceof Number
    }

    @Test
    void testInterval() {
        def val
        for (i in 0..3) { val = i }
        assert 3 == val
    }

    @Test
    void testAsType() {
        assert "12" as int == 12
        assert ("12" as Long) instanceof Long
        assert (null as Integer) == null
    }

    @Test(expected=NumberFormatException.class)
    void testAsOperatorFails() {
        "" as Integer
    }

    def methodReturnsMultiValues() {
        return ['hello', 5, new Date()]
    }

    @Test
    void testMultiReturn() {
        def str, intValue, now
        (str, intValue, now) = methodReturnsMultiValues()
        assert str == 'hello'
        assert intValue == 5
        assert now instanceof Date
    }

    @Test
    void testMapRelated() {
        def map = [ key1 : 'value1', key2 : 'value2' ]
        def keySet = map.keySet()
        assert keySet[0] instanceof String
        assert ['key1', 'key2'] == keySet as String[]
    }

    @Test
    void testConvertListToArray() {
        def list = []
        list << 'TWO' << 'ONE'
        assert list instanceof List
        def array = list as String[]
        assert array instanceof String[]
        assert array[0] == 'TWO'
        // a single string can be converted as an array too
        array = 'xxx' as String[]
        assert array instanceof String[]
    }

    @Test
    void testIntervalExclusive() {
        def val
        for (i in 0..<3) { val = i }
        assert 2 == val
    }

    @Test
    void testIntervalClosureUpto() {
        def val
        1.upto(5) { val = it }
        assert 5 == val
    }

    @Test
    void testRange() {
        def r = 1..10
        assert r.class.name == 'groovy.lang.IntRange'
        assert r.contains(1)
        assert ! r.contains(0)
        assert r.size() == 10
        assert r.from == 1
        assert r.to == 10
    }

    @Test
    void testRangeDefinedByVariable() {
        int lowerBound = 3, upperBound = 5
        Range r = lowerBound..upperBound
        assert r.contains(3)
        assert r.contains(5)
        assert ! r.contains(0)
        assert r.size() == 3
        assert r.from == 3
        assert r.to == 5
    }

    @Test
    void testRangeExlusiveUpperBound() {
        def r = 1..<10
        assert r.contains(1)
        assert ! r.contains(0)
        assert r.size() == 9
        assert r.from == 1
        assert r.to == 9
    }

    @Test
    void testRangeReverse() {
        def r = 1..3
        //println "r.class=${r.class.name}"
        assert r instanceof groovy.lang.IntRange
        assert r instanceof IntRange
        assert r instanceof Range
        def rv = r.reverse()
        //assert rv.class.name == 'groovy.lang.IntRange'
        assert rv instanceof List
        assert rv[0] == 3
        assert rv == [3, 2, 1]
        //def rvrange = new Range(rv)
        //println "rv=$rv"
        //println "rv.from=$rv.from"
    }

    @Test
    void testRegex() {
        def name = 'abc1.jpg'
        def matcher = (name =~ /([a-zA-Z]+)(\d.jpg)/)
        assert matcher instanceof java.util.regex.Matcher
        assert matcher.matches()
        assert matcher.groupCount() == 2
        assert matcher.group(0) == name
        assert matcher.group(1) == 'abc'
        assert matcher.group(2) == '1.jpg'
    }

    @Test
    void testRegexMoreGroovy() {
        def name = 'abc1.jpg'
        def matcher = (name =~ /([a-zA-Z]+)(\d\.jpg)/)
        assert matcher
        assert matcher[0].size() == 3
        assert matcher[0][0] == name
        assert matcher[0][1] == 'abc'
        assert matcher[0][2] == '1.jpg'
    }

    @Test
    void testRegexFind() {
        def name = 'abc1.jpg'
        def pattern = ~/\d+/ // different from "pattern =~ /..."
        assert pattern instanceof java.util.regex.Pattern
        def matcher = pattern.matcher(name)
        assert matcher instanceof java.util.regex.Matcher
        assert matcher.find()
        assert matcher.group() == '1'
        assert '123' =~ pattern
        assert 'a123b' =~ pattern // found in the middle
        assert ! ('ab' =~ pattern) // no digits found
        assert '123' ==~ pattern // matches the whole string
        assert ! ('a123b' ==~ pattern)
        assert ! ('ab' ==~ pattern)
    }

    @Test
    void testRegexBackupFiles() {
        def backupPattern = ~/^(.*)(~|\.bak)$/
        assert 'hello.txt~' ==~ backupPattern
        assert 'hello.txt.bak' ==~ backupPattern
        assert '.bak' ==~ backupPattern
        assert backupPattern.matcher('hello.txt~')
        assert ! backupPattern.matcher('hello.txt')
        assert backupPattern.matcher('hello.txt~')[0][0] == 'hello.txt~'
        assert backupPattern.matcher('hello.txt~')[0][1] == 'hello.txt'
        assert backupPattern.matcher('hello.txt~')[0][2] == '~'
        assert backupPattern.matcher('hello.txt.bak')[0][2] == '.bak'
    }

    @Test
    void testRegexMatchVersusFind() {
        def pattern = ~/([a-z]+)(\d.jpg)/
        def sample = 'Xabc1.jpg'
        def matcher = (sample ==~ pattern) // match
        assert ! matcher
        def finder = (sample =~ pattern) // find
        assert finder
    }

    @Test
    void testRegexTheGroovyWay() {
        def name = 'abc1.jpg'
        assert name =~ /\d+/ // find the '1'
        assertFalse name ==~ /\d+/ // but it doesn't match
        assert name ==~ /.*\.jpg/ // matches
        assert name =~ /.*\.jpg/ // if it matches then find returns always true
    }

    @Test
    void testRegexQuoting() {
        assert 'a.c' ==~ /a\.c/ //backslash before . to quote it
        assert '.{[()\\^$|?*+' ==~ /\.\{\[\(\)\\\^\$\|\?\*\+/
        //the 12 chars that need quoting
        assert '.{[()\\^$|?*+' ==~ /\Q.{[()\^$|?*+\E/
        //another way to quote text is to bracket with \Q and \E
        assert java.util.regex.Pattern.quote( /.{[()\^$|?*+/ ) == /\Q.{[()\^$|?*+\E/
        //a special method to quote text in this way
    }

    @Test
    void testArrayToFile() {
        // funny: an array of exactly two strings can be converted to a File object
        def f = ['aDirname','aFilename'] as File
        assert f instanceof File
        assert f.toString() == "aDirname${File.separator}aFilename"
    }

    @Test
    void testCapitalize() {
        assert 'hello'.capitalize() == 'Hello'
        assert '_hello'.capitalize() == '_hello'
        assert 'hELLO'.capitalize() == 'HELLO'
    }

    @Test
    void testStripIndent() {
        assert '  A\n B\nC' == '   A\n  B\n C'.stripIndent()
        String multiline = '''
                              A
                              B
                              C'''
        assert '\nA\nB\nC' == multiline.stripIndent()
        multiline =        '''\
                              A
                              B
                              C'''
        assert 'A\nB\nC' == multiline.stripIndent()
        multiline =        """\
                              A
                              B
                              C"""
        assert 'A\nB\nC' == multiline.stripIndent()
        def i = 5
        multiline =        """\
                              A$i
                              B
                              C"""
        assert 'A5\nB\nC' == multiline.stripIndent()
    }

    @Test
    void testWith() {
        'hello'.with {
            assert 5 == size()
            assert 5 == length()
            assert 'Hello' == capitalize()
        }
    }

    @Test
    void testBigIntegerToLongCast() {
        BigInteger b1 = new BigInteger("12")
        Long l1 = new Long(12L)
        Long l2 = new Long(13L)
        assert b1 == l1
        assert b1 != l2
        assert l1.equals((Long) b1) // groovy casts BigInteger to Long
        assert ! l2.equals((Long) b1) // groovy casts BigInteger to Long
    }

    @Test
    void testBigDecimalToLongCast() {
        BigDecimal b1 = new BigDecimal("12")
        Long l1 = new Long(12L)
        Long l2 = new Long(13L)
        assert b1 == l1
        assert b1 != l2
        assert l1.equals((Long) b1) // groovy casts BigDecimal to Long
        assert ! l2.equals((Long) b1) // groovy casts BigDecimal to Long
        // this will fail: assert l1.equals(b1)
    }

    @Test
    void testBigDecimalWithFractionToLongCast() {
        BigDecimal b1 = new BigDecimal("12.3")
        Long l1 = new Long(12L)
        assert b1 != l1
    }

    @Test
    void testBigDecimalwithFractionToLongCast2() {
        BigDecimal b1 = new BigDecimal("12.3")
        Long l1 = (Long) b1
        assert l1 == 12L
    }

    enum InfraMode { NO_INDEX_DROP, NO_RAISE_ERROR, NO_GRANT_EXPORT }
    @Test
    void testEnumSet() {
        def modeset = EnumSet.noneOf(InfraMode)
        for (enumString in 'NO_INDEX_DROP, NO_RAISE_ERROR'.split('\\s*,\\s*') ) {
            modeset << InfraMode.valueOf(enumString)
        }
        assert !modeset.contains(InfraMode.NO_GRANT_EXPORT)
        assert modeset.contains(InfraMode.NO_INDEX_DROP)
    }

    @Test
    void testCliBuilder() {
        def cli = new CliBuilder(usage:'CmdArgs [option]* filename')
        // this is the shortest possible option def: just the indicator char and a description
        cli.v('verbose')
        cli.h(longOpt: 'help', 'usage information')
        cli.c(argName: 'charset', args:1, longOpt: 'encoding', 'character encoding')
        cli.l(argName:'file', args:1, optionalArg:false,
                'use given file for log')
        cli.i(argName: 'extension', optionalArg: true,
                'modify files in place, create backup if extension is given (e.g. \'.bak\')')
        // Argumente der Kommandozeile (commandline) sind in args
        //def options = cli.parse(args)
        def options = cli.parse(['-llogfile', '-vh','-c','ASCII','arg1','arg2'])
        assert options.hasOption('h')
        assert options.h
        assert options.v
        assert options.c == 'ASCII'
        assert options.l == 'logfile'
        //println "remaining args ${options.getArgList()}"
        assert options.getArgList().size == 2
        assert options.getArgList() == ['arg1','arg2']
    }

    @Test
    @Ignore('prints some nasty output: "error: Missing required option"')
    void testCliBuilderRequiredArg() {
        def cli = new CliBuilder(usage:'programm -C file [option]*')
        // if required:true then options may be null
        cli.C(argName:'file', args:1, optionalArg:false, required:true, 'configuration property file')
        def options = cli.parse(['-C', '/path/to/file'])
        assert options
        assert '/path/to/file' == options.C
        options = cli.parse([]) // this will print usage info
        assert ! options
        options = cli.parse(['-C']) // this will print usage info, because args is not optional
        assert ! options
    }

    // Warning: XML resolver not found; external catalogs will be ignored
    //
    @Test @Ignore
    void testAntXMLSchemaValidateFailes() {
        def xmlDocument ='/home/armin/proj/wobcom/abis/src/AbisClient/AbisActivitiAdapter/test/unit/src/com/orangeobjects/bpm/activiti/testcase/Demo_08.bpmn20.xml'
        assert new File(xmlDocument).exists()
        def netbeansUserCatalog = '/home/armin/.netbeans/7.0/config/xml/catalogs/UserXMLCatalog.xml'
        assert new File(netbeansUserCatalog).exists()
        def ant = new AntBuilder()
        ant.xmlvalidate(file:xmlDocument, warn:'yes') {
            attribute( name:'http://xml.org/sax/features/validation', value:'true' )
            attribute( name:'http://apache.org/xml/features/validation/schema', value:'true' )
            attribute( name:'http://xml.org/sax/features/namespaces', value:'true' )
            xmlcatalog { catalogpath { pathelement(location: netbeansUserCatalog) } }
        }
    }

    // from /home/armin/.netbeans/7.0/config/xml/catalogs/UserXMLCatalog.xml
    //  <system systemId="http://www.omg.org/spec/BPMN/2.0/20100501/BPMN20.xsd" uri="file:/usr/local/opt/activiti-5.6/docs/xsd/BPMN20.xsd"/>
    //  <system systemId="http://activiti.org/bpmn" uri="file:/usr/local/opt/activiti-5.6/docs/xsd/activiti-bpmn-extensions-5.4.xsd"/>
    @Test @Ignore
    void testAntXMLSchemaValidate() {
        String xmlDocument = checkFileCanRead('/home/armin/proj/wobcom/abis/src/AbisClient/AbisActivitiAdapter/test/unit/src/com/orangeobjects/bpm/activiti/testcase/Demo_08.bpmn20.xml')
        new AntBuilder().schemavalidate(file:xmlDocument, warn:'yes') {
            schema(namespace:'http://www.omg.org/spec/BPMN/20100524/MODEL', file:'/usr/local/opt/activiti-5.6/docs/xsd/BPMN20.xsd')
            schema(namespace:'http://activiti.org/bpmn', file:'/usr/local/opt/activiti-5.6/docs/xsd/activiti-bpmn-extensions-5.4.xsd')
        }
    }

    @Test
    void testSerialBlob() {
        def byte[] bites = 'abc'.bytes
        def blob = new javax.sql.rowset.serial.SerialBlob(bites)
        assert bites == blob.binaryStream.bytes
        assert blob.length() == bites.length
        // Note: 1L is the first postion
        assert bites == blob.getBytes(1L, blob.length() as int)
        assert bites == blob.getBytes(1L, (int)blob.length())
    }

    @Test
    void testConfigSlurper() {
        def propertiesString = '''
        path = /home/dummyuser/doc
        document = ${path}/hello.txt
        # Bob the Builder
        user.name = Bob
        user.job = Builder
        intvalue = 12
        multiline = line1 \
                    line2
        '''
        Properties props = new Properties()
        def stream = new ByteArrayInputStream(propertiesString.bytes)
        props.load(stream)

        assert props.path == '/home/dummyuser/doc'
        assert props.document == '${path}/hello.txt'
        assert props.'user.name' == 'Bob'
        assert props.intvalue == '12'
        assert props.intvalue as int == 12
        assert props.multiline.split() == ['line1','line2']
        assert props.notExists == null
        assert !props.notExists

        def config = new ConfigSlurper().parse(props)
        assert config.getClass() == groovy.util.ConfigObject.class
        assert config.path == '/home/dummyuser/doc'
        assert config.document == '${path}/hello.txt'
        assert config.multiline.split() == ['line1','line2']
        assert config.user.name == 'Bob'
        assert config.notExists == [:]
        assert !config.notExists
    }

    // helper
    static String checkFileCanRead(String fn) {
        assert new File(fn).canRead()
        return fn
    }
}
