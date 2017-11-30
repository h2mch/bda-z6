package ch.hslu.cas.bda.ingestion.bitcoin;

import ch.hslu.cas.bda.message.avro.AvroSerializer;
import ch.hslu.cas.bda.message.avro.BlockConverter;
import ch.hslu.cas.bda.message.bitcoin.AvBlock;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;


public class BlockToAvroBlockTransformer implements IBlockProcessor {

    private static final String BITCOIN_AVRO_FILES = "/Users/had/temp/Bitcoin/avblocks/";

    private static final String BITCOIN_BLOCKS = "/Users/had/Library/Application Support/Bitcoin/blocks/";

    public static void main(String[] args) throws IOException {

        List<File> blockChainFiles = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(BITCOIN_BLOCKS))) {

            StreamSupport.stream(directoryStream.spliterator(), false)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith("dat"))
                    .filter(path -> path.getFileName().toString().startsWith("blk0"))
                    .forEach(path -> blockChainFiles.add(path.toFile()));
        }
        BlockChainProcessorExecutor processor = new BlockChainProcessorExecutor(new BlockToAvroBlockTransformer());
        processor.processBlocks(blockChainFiles);
    }

    @Override
    public void process(long blockCount, Block block) throws IOException {
        String filename = BITCOIN_AVRO_FILES + "msg" + String.format("%09d", blockCount);
        AvBlock avBlock = new BlockConverter().toAvBlock(block, blockCount);
        new AvroSerializer().toFile(avBlock, AvBlock.class, filename);

    }
}

