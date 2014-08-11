package org.familysearch.standards.place.ws.helper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.familysearch.standards.core.logging.Logger;


/**
 * Utility to test accessors (setters and getters; conforming to the Java Bean
 * specification).
 * <p>
 * Default parameter values exist for the following primitive types:
 * <ul>
 * <li>boolean</li>
 * <li>byte</li>
 * <li>char</li>
 * <li>int</li>
 * <li>long</li>
 * <li>double</li>
 * <li>float</li>
 * <li>short</li>
 * </p>
 * <p>
 * Default parameter values exist for the following standard classes and interfaces:
 * <ul>
 * <li>Boolean</li>
 * <li>Byte</li>
 * <li>Character</li>
 * <li>Integer</li>
 * <li>Long</li>
 * <li>Double</li>
 * <li>Float</li>
 * <li>Short</li>
 * <li>Map</li>
 * <li>List</li>
 * <li>Set</li>
 * <li>Collection</li>
 * <li>Calendar</li>
 * </ul>
 * </p>
 * @author fransonsr
 *
 */
public class POJOTestUtil {

    private static final Logger log = new Logger(POJOTestUtil.class);

    private static String[] classExclusionFilters = {
        ".*[$\\-].*",   // exclude inner-classes, package-info.java
        "(?i).*test.*"  // any package or class with '[Tt]est' in the name
    };

    /**
     * Return the default list of class exclusion filters.
     * @return
     */
    public static List<String> defaultClassExclusionFilters() {
        return new ArrayList<String>(Arrays.asList(classExclusionFilters));
    }

    /**
     * Test the specified class. This assumes all parameter types
     * are well known and there are no exclusions.
     * @param classUnderTest
     * @throws Exception if class could not be instantiated
     */
    public static void test(Class<?> classUnderTest) throws Exception {
        new POJOTestUtil(classUnderTest).test();
    }

    /**
     * Test the specified object. This assumes all parameter types
     * are well known and there are no exclusions.
     * @param objectUnderTest
     */
    public static void test(Object objectUnderTest) {
        new POJOTestUtil(objectUnderTest).test();
    }

    /**
     * Test all classes in the specified package. Classes with 'test' or 'Test'
     * in the name are excluded. This assumes all parameter types are well
     * known and there are no exclusions.
     * @param thePackage
     */
    public static void test(Package thePackage) {
        test(thePackage.getName());
    }

    /**
     * Test all classes in the specified package. Classes with 'test' or 'Test'
     * in the name are excluded. This assumes all parameter types are well
     * known and there are no exclusions.
     * @param thePackage
     */
    public static void test(String thePackage) {
        List<String> exclusions = new ArrayList<String>();
        for(String exclusion : classExclusionFilters) {
            exclusions.add(exclusion);
        }

        test(thePackage, exclusions);
    }

    /**
     * Test all classes in the specified package using the list of regex
     * exclusion patterns. Classes matching one of these patterns is excluded
     * from testing. This assumes all parameter types are well
     * known and there are no method-level exclusions.
     * @param thePackage
     * @param exclusions list of regular expression patterns to match against class names to exclude
     */
    public static void test(Package thePackage, List<String> exclusions) {
        test(thePackage.getName(), exclusions);
    }

    /**
     * Test all classes in the specified package using the list of regex
     * exclusion patterns. Classes matching one of these patterns is excluded
     * from testing. This assumes all parameter types are well
     * known and there are no method-level exclusions.
     * @param thePackage
     * @param exclusions list of regular expression patterns to match against class names to exclude
     */
    public static void test(String thePackage, List<String> exclusions) {
        try {
            List<Class<?>> classes = new ArrayList<Class<?>>();

            String packagePath = thePackage.replace('.', File.separatorChar);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(packagePath);

            List<Pattern> exclusionPatterns = new ArrayList<Pattern>();
            for(String exclusion : exclusions) {
                exclusionPatterns.add(Pattern.compile(exclusion));
            }

            while(resources.hasMoreElements()) {
                String resource = resources.nextElement().getFile();
                File file = new File(URLDecoder.decode(resource, "UTF-8"));

                addClassFiles(thePackage, file, classes, exclusionPatterns);
            }

            for(Class<?> classUnderTest : classes) {
                try {
                    test(classUnderTest);
                } catch (Exception e) {
                    log.error("Failed to create instance of class: " + classUnderTest);
                }
            }
        } catch (IOException e) {
            log.error("Failed to obtain list of classes for package: " + thePackage, e);
        }
    }

    /**
     * Examine the specified file adding all classes to the list.
     * @param thePackage
     * @param file
     * @param classes
     */
    private static void addClassFiles(String thePackage, File file, List<Class<?>> classes, List<Pattern> exclusions) {
        if (file.isDirectory()) {
            for(File subFile : file.listFiles()) {
                addClassFiles(thePackage, subFile, classes, exclusions);
            }
        }
        else {
            String filename = file.getName();
            if (filename.endsWith(".class")) {
                String name = filename.substring(0, filename.indexOf(".class"));
                String className = thePackage + "." + name;

                for(Pattern pattern : exclusions) {
                    if (pattern.matcher(className).matches()) {
                        log.debug("Excluding class: " + className + "; by exclusion rule: " + pattern.toString());
                        return;
                    }
                }

                try {
                    Class<?> theClass = Class.forName(className);
                    classes.add(theClass);

                    log.debug("Added class: " + theClass);
                } catch (ClassNotFoundException e) {
                    // do nothing here
                }
            }
        }
    }

    protected Class<?> classUnderTest;
    protected Object objectUnderTest;
    protected List<String> exclusions = new ArrayList<String>();
    protected List<String> propertiesSet = new ArrayList<String>();
    protected Map<Class<?>, Object> parameterValues = new HashMap<Class<?>, Object>();

    /**
     * Test the specified class. This method will use the default
     * constructor to construct an instance of the class.
     * @param classUnderTest the class under test
     * @throws Exception if an instance could not be created
     */
    public POJOTestUtil(Class<?> classUnderTest) throws Exception {
        this.classUnderTest = classUnderTest;
        objectUnderTest = classUnderTest.newInstance();
        initialParameterValues();
    }

    /**
     * Test the specified object.
     * @param objectUnderTest object under test
     */
    public POJOTestUtil(Object objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
        this.classUnderTest = objectUnderTest.getClass();
        initialParameterValues();
    }

    /**
     * Set initial parameter values for well known interfaces and
     * classes without a default constructor.
     */
    protected void initialParameterValues() {
        parameterValues.put(Boolean.class, Boolean.valueOf(true));
        parameterValues.put(boolean.class, Boolean.valueOf(true));
        parameterValues.put(Byte.class, Byte.valueOf((byte)1));
        parameterValues.put(byte.class, Byte.valueOf((byte)3));
        parameterValues.put(Character.class, Character.valueOf((char)5));
        parameterValues.put(char.class, Character.valueOf((char)7));
        parameterValues.put(Integer.class, Integer.valueOf(11));
        parameterValues.put(int.class, Integer.valueOf(13));
        parameterValues.put(Long.class, Long.valueOf(17));
        parameterValues.put(long.class, Long.valueOf(19));
        parameterValues.put(Double.class, Double.valueOf(23.0));
        parameterValues.put(double.class, Double.valueOf(29.0));
        parameterValues.put(Float.class, Float.valueOf(31.0f));
        parameterValues.put(float.class, Float.valueOf(37.0f));
        parameterValues.put(Short.class, Short.valueOf((short)41));
        parameterValues.put(short.class, Short.valueOf((short)43));
        parameterValues.put(Map.class, new HashMap<Object, Object>());
        parameterValues.put(List.class, new ArrayList<Object>());
        parameterValues.put(Set.class, new HashSet<Object>());
        parameterValues.put(Collection.class, new ArrayList<Object>());
        parameterValues.put(Calendar.class, new GregorianCalendar());
        parameterValues.put(String.class,"");
    }

    /**
     * Add a parameter value; use this only if the parameter value's
     * type is the actual type expected (rather than, say, an interface).
     * This is needed when the type does not have a default (no-arg)
     * constructor or a specific value should be used during the test.
     * (For example, an enumeration.)
     * @param parameterValue
     */
    public void addParameterValue(Object parameterValue) {
        addParameterValue(parameterValue.getClass(), parameterValue);
    }

    /**
     * Add a parameter value for the specified type. This is needed when the
     * type does not have a default (no-arg) constructor (such as an interface)
     * or a specific value should be used during the test.
     * @param parameterType
     * @param parameterValue
     */
    public void addParameterValue(Class<?> parameterType, Object parameterValue) {
        parameterValues.put(parameterType, parameterValue);
    }

    /**
     * Add the specified parameter values to the map of existing parameter values.
     * @param parameterValues
     */
    public void addParameterValues(Map<Class<?>, Object> parameterValues) {
        this.parameterValues.putAll(parameterValues);
    }

    /**
     * Add a list of method names to the exclusion list.
     * @param methodNames
     */
    public void addExclusions(String ...methodNames) {
        for(String methodName : methodNames) {
            exclusions.add(methodName);
        }
    }

    /**
     * Add a collection of method names to the exclusion list.
     * @param methodNames
     */
    public void addExclusions(Collection<String> methodNames) {
        exclusions.addAll(methodNames);
    }

    /**
     * Test the class/object. The order of execution is:
     * <ol>
     * <li>setXXX methods</li>
     * <li>getXXX methods</li>
     * <li>isXXX methods</li>
     * <li>toString method</li>
     * <li>hashCode method</li>
     * <li>equals method</li>
     * </ol>
     * <p>
     * NOTE: only public methods are invoked. Additional testing may be required
     * for non-public methods.
     * </p>
     * <p>
     * NOTE: if a parameter value is set with the setXXX method the return
     * from the getXXX/isXXX method is validated; otherwise, the return is
     * ignored. If the setXXX method does other than a simple assignment
     * additional testing may be required or the methods added to the
     * exclusion list.
     * </p>
     * <p>
     * NOTE: only simple testing is done with hashCode and equals methods.
     * Additional testing may be required of these methods.
     * </p>
     */
    public void test() {
        testSetMethods();
        testGetMethods();
        testIsMethods();
        testToString();
        testHashCode();
        testEquals();
    }

    /**
     * Create a new instance of the specified class using the
     * default constructor.
     * <p>
     * NOTE: override this method to have your favorite mocking / proxy
     * factory utility create an instance!
     * </p>
     * @param theClass
     * @return new instance
     * @throws Exception if the instance cannot be created.
     */
    protected Object createNewInstance(Class<?> theClass) throws Exception {
        Constructor<?> constructor = theClass.getConstructor((Class<?>[])null);

        return constructor.newInstance((Object[])null);
    }

    /**
     * Invoke all public setXXX methods for which we have a parameter value for.
     */
    private void testSetMethods() {
        log.debug("testSetMethods for class:" + classUnderTest);
        for(Method method : classUnderTest.getDeclaredMethods()) {
            if (exclusions.contains(method.getName())) {
                log.debug("Method excluded: " + method);
                continue;
            }

            if (!method.getName().startsWith("set")) {
                log.debug("Skipping non-set method: " + method);
                continue;
            }

            if (!Modifier.isPublic(method.getModifiers())) {
                log.debug("Skipping non-public method: " + method);
                continue;
            }

            if (method.getParameterTypes().length > 1) {
                log.debug("Unexpected number of parameters; skipping method: " + method);
                continue;
            }

            Class<?> parameterType = method.getParameterTypes()[0];
            if (!parameterValues.containsKey(parameterType)) {
                log.debug("Attempting to create parameter instance of type: " + parameterType);
                try {
                    Object parameterValue = createNewInstance(parameterType);

                    parameterValues.put(parameterType, parameterValue);
                } catch (Exception e) {
                    log.debug("Unable to create parameter instance of type: " + parameterType + "; skipping method: " + method, e);
                    continue;
                }
            }

            Object parameterValue = parameterValues.get(parameterType);
            if (parameterValue == null) {
                log.debug("No parameter value for type: " + parameterType + "; skipping method: " + method);
                continue;
            }

            try {
                log.debug("Attempting to invoke method: " + method);
                method.invoke(objectUnderTest, parameterValue);
                propertiesSet.add(method.getName().substring(3));
            } catch (Exception e) {
                log.debug("Unable to invoke method: " + method, e);
            }
        }
    }

    /**
     * Invoke all public getXXX methods. If the matching setXXX method
     * was called, the return value of the getXXX method is validated.
     */
    private void testGetMethods() {
        log.debug("testGetMethods for class:" + classUnderTest);
        testGetter("get");
    }

    /**
     * Invoke all public isXXX methods. If the matching setXXX method was called,
     * the return value is validated.
     */
    private void testIsMethods() {
        log.debug("testIsMethods for class:" + classUnderTest);
        testGetter("is");
    }

    /**
     * Test the getter method (getXXX/isXXX).
     */
    private void testGetter(String prefix) {
        for(Method method : classUnderTest.getDeclaredMethods()) {
            if (exclusions.contains(method.getName())) {
                log.debug("Method excluded: " + method);
                continue;
            }

            if (!method.getName().startsWith(prefix)) {
                log.debug(String.format("Skipping non-%s method: %s", prefix, method));
                continue;
            }

            if (!Modifier.isPublic(method.getModifiers())) {
                log.debug("Skipping non-public method: " + method);
                continue;
            }

            try {
                log.debug("Attempting to invoke method: " + method);
                Object actual = method.invoke(objectUnderTest);

                if (propertiesSet.contains(method.getName().substring(prefix.length()))) {
                    Class<?> returnType = method.getReturnType();
                    log.debug("Validating return value to method: " + method);
                    if (!parameterValues.containsKey(returnType)) {
                        assert false: "Unknown return type: " + returnType + "; method: " + method;
                    }

                    assert this.testAssertEquals(parameterValues.get(returnType), actual) : "Parameter value mismatch";
//                    assertEquals(parameterValues.get(returnType), actual);
                }
            } catch (Exception e) {
                log.debug("Unable to invoke method: " + method, e);
            }
        }
    }

    /**
     * Invoke the toString method.
     */
    private void testToString() {
        if (exclusions.contains("toString")) {
            log.debug("Method excluded: toString");
        }

        assert objectUnderTest.toString() != null;
    }

    /**
     * Invoke the hashCode method.
     */
    private void testHashCode() {
        if (exclusions.contains("hashCode")) {
            log.debug("Method excluded: hashCode");
        }

        objectUnderTest.hashCode();
    }

    /**
     * Invoke the equals method with simple arguments.
     */
    private void testEquals() {
        if (exclusions.contains("equals")) {
            log.debug("Method excluded: equals");
        }

        assert (objectUnderTest.equals(objectUnderTest));
        assert (! objectUnderTest.equals(null));
        assert (! objectUnderTest.equals(new Object()));
    }

    /**
     * Test equality of two objects, either of which may be null, either of which may
     * be of any class or even primitive type.
     * 
     * @param obj1 first object, or null
     * @param obj2 second object, or null
     * @return TRUE if they are equal, FALSE otherwise
     */
    private boolean testAssertEquals(Object obj1, Object obj2) {
        if (obj1 == null  &&  obj2 == null) {
            return true;
        } else if (obj1 == null  ||  obj2 == null) {
            return false;
        } else if (! obj1.getClass().equals(obj2.getClass())) {
            return false;
        }

        if (obj1.getClass().isPrimitive()) {
            return obj1 == obj2;
        } else {
            return obj2.equals(obj2);
        }
    }
}
