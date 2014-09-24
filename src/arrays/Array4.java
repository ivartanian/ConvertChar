package arrays;

class Base {
    public String name = "Base";

    public void f() {
        System.out.println("Base.f()");
    }

}

class Sub extends Base {
    public String name = "Sub";

    @Override
    public void f() {
        System.out.println("Sub.f()");
    }
}

class Program {
    public static void main(String[] args) {
        Sub s = new Sub();
        Base b = s;
        System.out.println(b.name);
        b.f();
    }
}