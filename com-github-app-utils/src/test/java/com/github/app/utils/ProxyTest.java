package com.github.app.utils;

import org.apache.commons.proxy.Invoker;
import org.apache.commons.proxy.ObjectProvider;
import org.apache.commons.proxy.ProxyFactory;
import org.apache.commons.proxy.invoker.DuckTypingInvoker;

import java.lang.reflect.Method;

public class ProxyTest {
    public interface Echo {
        void echo();
    }

    public static class EchoImpl implements Echo {

        @Override
        public void echo() {
            System.out.println("hellow proxy");
        }
    }

    public static class EchoProvider implements ObjectProvider {
        private Echo echo;

        public EchoProvider(Echo echo) {
            this.echo = echo;
        }

        @Override
        public Object getObject() {
            return echo;
        }
    }

    public static class CommonsInvoker {
        private ProxyFactory factory;
        private static final Class[] ECHO_ONLY = new Class[]{Echo.class};

        public CommonsInvoker() {
            this.factory = new ProxyFactory();
        }

        public void test() {
            EchoProvider provider = new EchoProvider(new EchoImpl());
            final Echo echo = (Echo) factory.createInvokerProxy(new DuckTypingInvoker(provider), ECHO_ONLY);
            echo.echo();
        }
    }

    public static void main(String[] args) {
        new CommonsInvoker().test();
    }
}
