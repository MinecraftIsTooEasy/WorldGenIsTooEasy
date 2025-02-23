package rwg.world;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.MathHelper;
import net.minecraft.World;

import java.util.Random;

public class MapGenCaves2 extends MapGenBase2 {

    protected void addRoom(long seed, int cx, int cz, Block[] blocks, double x, double y, double z) {
        this.addTunnel(seed, cx, cz, blocks, x, y, z, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void addTunnel(long seed, int cx, int cz, Block[] blocks, double x, double y, double z, float width, float yaw, float pitch, int startCounter, int endCounter, double heightModifier) {
        double d4 = cx * 16 + 8;
        double d5 = cz * 16 + 8;
        float f3 = 0.0F;
        float f4 = 0.0F;
        Random random = new Random(seed);

        if (endCounter <= 0) {
            int j1 = this.range * 16 - 16;
            endCounter = j1 - random.nextInt(j1 / 4);
        }

        boolean flag2 = false;

        if (startCounter == -1) {
            startCounter = endCounter / 2;
            flag2 = true;
        }

        int k1 = random.nextInt(endCounter / 2) + endCounter / 4;

        for (boolean flag = random.nextInt(6) == 0; startCounter < endCounter; ++startCounter) {
            double d6 = 1.5D + (double) (MathHelper.sin((float) startCounter * (float) Math.PI / (float) endCounter) * width * 1.0F);
            double d7 = d6 * heightModifier;
            float f5 = MathHelper.cos(pitch);
            float f6 = MathHelper.sin(pitch);
            x += MathHelper.cos(yaw) * f5;
            y += f6;
            z += MathHelper.sin(yaw) * f5;

            if (flag) {
                pitch *= 0.92F;
            } else {
                pitch *= 0.7F;
            }

            pitch += f4 * 0.1F;
            yaw += f3 * 0.1F;
            f4 *= 0.9F;
            f3 *= 0.75F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!flag2 && startCounter == k1 && width > 1.0F && endCounter > 0) {
                this.addTunnel(random.nextLong(), cx, cz, blocks, x, y, z, random.nextFloat() * 0.5F + 0.5F, yaw - ((float) Math.PI / 2F), pitch / 3.0F, startCounter, endCounter, 1.0D);
                this.addTunnel(random.nextLong(), cx, cz, blocks, x, y, z, random.nextFloat() * 0.5F + 0.5F, yaw + ((float) Math.PI / 2F), pitch / 3.0F, startCounter, endCounter, 1.0D);
                return;
            }

            if (flag2 || random.nextInt(4) != 0) {
                double d8 = x - d4;
                double d9 = z - d5;
                double d10 = endCounter - startCounter;
                double d11 = width + 2.0F + 16.0F;

                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
                    return;
                }

                if (x >= d4 - 16.0D - d6 * 2.0D && z >= d5 - 16.0D - d6 * 2.0D && x <= d4 + 16.0D + d6 * 2.0D && z <= d5 + 16.0D + d6 * 2.0D) {
                    int i4 = MathHelper.floor_double(x - d6) - cx * 16 - 1;
                    int l1 = MathHelper.floor_double(x + d6) - cx * 16 + 1;
                    int j4 = MathHelper.floor_double(y - d7) - 1;
                    int i2 = MathHelper.floor_double(y + d7) + 1;
                    int k4 = MathHelper.floor_double(z - d6) - cz * 16 - 1;
                    int j2 = MathHelper.floor_double(z + d6) - cz * 16 + 1;

                    if (i4 < 0) {
                        i4 = 0;
                    }

                    if (l1 > 16) {
                        l1 = 16;
                    }

                    if (j4 < 1) {
                        j4 = 1;
                    }

                    if (i2 > 248) {
                        i2 = 248;
                    }

                    if (k4 < 0) {
                        k4 = 0;
                    }

                    if (j2 > 16) {
                        j2 = 16;
                    }

                    boolean flag3 = false;
                    int k2;
                    int j3;

                    for (k2 = i4; !flag3 && k2 < l1; ++k2) {
                        for (int l2 = k4; !flag3 && l2 < j2; ++l2) {
                            for (int i3 = i2 + 1; !flag3 && i3 >= j4 - 1; --i3) {
                                j3 = (k2 * 16 + l2) * 256 + i3;

                                if (i3 < 256) {

                                    if (isOceanBlock(blocks, j3, k2, i3, l2, cx, cz)) {
                                        flag3 = true;
                                    }

                                    if (i3 != j4 - 1 && k2 != i4 && k2 != l1 - 1 && l2 != k4 && l2 != j2 - 1) {
                                        i3 = j4;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag3) {
                        for (k2 = i4; k2 < l1; ++k2) {
                            double d13 = ((double) (k2 + cx * 16) + 0.5D - x) / d6;

                            for (j3 = k4; j3 < j2; ++j3) {
                                double d14 = ((double) (j3 + cz * 16) + 0.5D - z) / d6;
                                int k3 = (k2 * 16 + j3) * 256 + i2;
                                boolean flag1 = false;

                                if (d13 * d13 + d14 * d14 < 1.0D) {
                                    for (int l3 = i2 - 1; l3 >= j4; --l3) {
                                        double d12 = ((double) l3 + 0.5D - y) / d7;

                                        if (d12 > -0.7D && d13 * d13 + d12 * d12 + d14 * d14 < 1.0D) {
                                            if (isTopBlock(blocks, k3, k2, l3, j3, cx, cz)) {
                                                flag1 = true;
                                            }
                                            digBlock(blocks, k3, k2, l3, j3, cx, cz, flag1);
                                        }

                                        --k3;
                                    }
                                }
                            }
                        }

                        if (flag2) {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void recursiveGenerate(World worldIn, int dx, int dz, int cx, int cz, Block[] blocks) {
        BiomeGenBase biome = worldIn.getBiomeGenForCoords(dx * 16, dz * 16);
        float frequency;
        if (biome != BiomeGenBase.plains && biome != BiomeGenBase.swampland) {
            if (biome == BiomeGenBase.iceMountains) {
                frequency = 1.2F;
            } else if (biome == BiomeGenBase.extremeHills) {
                frequency = 1.4F;
            } else {
                frequency = 1.0F;
            }
        } else {
            frequency = 0.8F;
        }

        int i1 = 0;
        if (this.rand.nextInt((int) (15F / frequency)) == 0) {
            i1 = (int) (this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(30) + 1) + 1) * frequency);
        }

        for (int j1 = 0; j1 < i1; ++j1) {
            double d0 = dx * 16 + this.rand.nextInt(16);
            double d1 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            double d2 = dz * 16 + this.rand.nextInt(16);
            int k1 = 1;

            int rarity_of_large_tunnels = d1 > 23.0 && d1 < 33.0 ? 2 : 10;

            if (this.rand.nextInt(4) == 0) {
                this.addRoom(this.rand.nextLong(), cx, cz, blocks, d0, d1, d2);
                k1 += this.rand.nextInt(4);
            }

            for (int l1 = 0; l1 < k1; ++l1) {
                float f = this.rand.nextFloat() * (float) Math.PI * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(rarity_of_large_tunnels) == 0) {
                    f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                    if (rarity_of_large_tunnels < 10) {
                        f2 *= 1.0F + this.rand.nextFloat() * 0.5F;
                    }
                }

                this.addTunnel(this.rand.nextLong(), cx, cz, blocks, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
            }
        }
    }

    protected boolean isOceanBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ) {
        return data[index] == Block.waterMoving || data[index] == Block.waterStill;
    }

    protected boolean isExceptionBiome(BiomeGenBase biome) {
        return biome == BiomeGenBase.desert || biome == BiomeGenBase.beach;
    }

    private boolean isTopBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ) {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
        return (isExceptionBiome(biome) ? data[index] == Block.grass : data[index] == Block.blocksList[biome.topBlock]);
    }

    protected void digBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop) {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
        Block top = (isExceptionBiome(biome) ? Block.grass : Block.blocksList[biome.topBlock]);
        Block filler = (isExceptionBiome(biome) ? Block.dirt : Block.blocksList[biome.fillerBlock]);
        Block block = data[index];

        if (this.canReplaceBlock(block, null) || block == filler || block == top) {
            if (y < 10) {
                data[index] = Block.lavaStill;
            } else {
                data[index] = null;

                if (foundTop && data[index - 1] == filler) {
                    data[index - 1] = top;
                }
            }
        }
    }
}
