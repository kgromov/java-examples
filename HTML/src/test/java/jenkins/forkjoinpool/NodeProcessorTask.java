package jenkins.forkjoinpool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class NodeProcessorTask extends RecursiveAction {
    private static final int DEFAULT_PRODUTCR_SIZE = 10;

    private NodeList xmlNodes;
    private int first;
    private int last;

    public NodeProcessorTask(NodeList xmlNodes, int first, int last) {
        this.xmlNodes = xmlNodes;
        this.first = first;
        this.last = last;
    }

    @Override
    protected void compute() {
        if (last - first < DEFAULT_PRODUTCR_SIZE) {
            processNodes();
        } else {
            int middle = (last + first) / 2;
            System.out.printf("NodeProcessorTask: Pending tasks:%s\n", getQueuedTaskCount());
            NodeProcessorTask t1 = new NodeProcessorTask(xmlNodes, first, middle + 1);
            NodeProcessorTask t2 = new NodeProcessorTask(xmlNodes, middle + 1, last);
            invokeAll(t1, t2);
        }
    }

    private void processNodes() {
        for (int i = first; i < last; i++) {
            Element subParent = (Element) xmlNodes.item(i);
            // seems it's better make algorithm recursive but currently remains the same
            NodeList childNodes = subParent.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node childNode = childNodes.item(j);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    System.out.println(String.format("Thread = %s, subRoot index = %d, child text = %s",
                            Thread.currentThread(), i, childNode.getTextContent()));
                }
            }
        }
    }

    private static void processSequentially(NodeList xmlNodes)
    {
        for (int i = 0; i < xmlNodes.getLength(); i++) {
            Element subParent = (Element) xmlNodes.item(i);
            // seems it's better make algorithm recursive but currently remains the same
            NodeList childNodes = subParent.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node childNode = childNodes.item(j);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    System.out.println(String.format("Thread = %s, subRoot index = %d, child text = %s",
                            Thread.currentThread(), i, childNode.getTextContent()));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();
        File xmlFile = new File("C:\\Projects\\java-examples\\HTML\\target\\jenkins-builds\\index.xml");
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = dBuilder.parse(xmlFile);

        NodeList builds = document.getElementsByTagName("Build");
        NodeProcessorTask task = new NodeProcessorTask(builds, 0, builds.getLength());
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(task);

        do {
            System.out.printf("Main: Thread Count:%d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Thread Steal:%d\n", pool.getStealCount());
            System.out.printf("Main: Parallelism:%d\n", pool.getParallelism());
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());

        pool.shutdown();

        System.out.println(String.format("Time elapsed = %d", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));

        start = System.nanoTime();
        processSequentially(builds);
        System.out.println(String.format("Time elapsed = %d", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
    }
}
