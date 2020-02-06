abstract class Sample {
    Sample() {
        System.out.println("Hello World");
    }

    int method1(int a, int b) {
        return a + b;
    }

    int method2(int a, int b) {
        return b + a;
    }

    // These methods will be unchecked
    abstract int method3();

    void method4() {
    }
}
