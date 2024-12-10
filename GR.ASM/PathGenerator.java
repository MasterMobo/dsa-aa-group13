import java.util.Random;

public class PathGenerator {
    private static final String selection = "URLD";
    private static final String inputSelection = "URLD*";
    private int length = 63;

    public PathGenerator() {
    }

    public PathGenerator(int length) {
        this.length = length;
    }

    public String randomPath() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            builder.append(selection.charAt(random.nextInt(selection.length())));
        }

        return builder.toString();
    }

    public String randomInputPath() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            builder.append(inputSelection.charAt(random.nextInt(inputSelection.length())));
        }

        return builder.toString();
    }

    public String fill(char c) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(c);
        }

        return builder.toString();
    }
}
