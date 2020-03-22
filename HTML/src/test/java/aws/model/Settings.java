package aws.model;

import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Getter
public class Settings
{
    private Set<String> products;
    private String updateRegion;
    private String map1Path;
    private String map2Path;
    private String outputDir;

    private Settings(Builder builder)
    {
      this.products = builder.products;
      this.updateRegion = builder.updateRegion;
      this.map1Path = builder.map1Path;
      this.map2Path = builder.map2Path;
      this.outputDir = builder.outputDir == null ? Paths.get(".").normalize().toAbsolutePath().toString() : builder.outputDir;
    }

    public Path getLogsFolder()
    {
        return Paths.get(outputDir).resolve("logs");
    }

    public Path getResultsFolder()
    {
        return Paths.get(outputDir).resolve("results");
    }

    public static String getVersion(String path, String defaultValue)
    {
        int startIndex = path.indexOf("Akela");
        int endIndex = path.indexOf("/logs");
        return startIndex != -1 && endIndex != -1 ? path.substring(startIndex, endIndex) : defaultValue;
    }

    public static final class Builder
    {
        private Set<String> products;
        private String updateRegion;
        private String map1Path;
        private String map2Path;
        private String outputDir;

        public Settings build()
        {
            return new Settings(this);
        }

        public Builder product(Set<String> products)
        {
            this.products = products;
            return this;
        }

        public Builder updateRegion(String updateRegion)
        {
            this.updateRegion = updateRegion;
            return this;
        }

        public Builder map1Path(String map1Path)
        {
            this.map1Path = map1Path;
            return this;
        }

        public Builder map2Path(String map2Path)
        {
            this.map2Path = map2Path;
            return this;
        }

        public Builder outputDir(String outputDir)
        {
            this.outputDir = outputDir;
            return this;
        }
    }
}
