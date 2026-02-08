import random


# --- PART 1: A3/A8 (Generating the Keys) ---
def generate_keys():
    # 128-bit Secret Key (Ki) and Random Challenge (RAND)
    ki_int = random.getrandbits(128)
    rand_int = random.getrandbits(128)


    # Convert to 128-bit binary strings
    ki_bin = bin(ki_int)[2:].zfill(128)
    rand_bin = bin(rand_int)[2:].zfill(128)


    # Splitting into Left (64-bit) and Right (64-bit)
    ki_left, ki_right = ki_bin[:64], ki_bin[64:]
    rand_left, rand_right = rand_bin[:64], rand_bin[64:]


    # Mixing (A3 Logic for SRES)
    mix_1 = int(ki_left, 2) ^ int(rand_right, 2)
    mix_2 = int(ki_right, 2) ^ int(rand_left, 2)
    combined = bin(mix_1 ^ mix_2)[2:].zfill(64)


    # SRES is 32-bit (XORing the two halves of the combined result)
    sres = int(combined[:32], 2) ^ int(combined[32:], 2)
    
    # Kc is the 64-bit Session Key
    kc_bin = combined # Using the 64-bit mixed result as our session key
    
    return sres, kc_bin


# --- PART 2: A5/1 (The Encryption Engine) ---
class A5_1_Cipher:
    def __init__(self, kc):
        # Initialize 3 registers with bits from the 64-bit Kc
        self.r1 = [int(b) for b in kc[0:19]]
        self.r2 = [int(b) for b in kc[19:41]]
        self.r3 = [int(b) for b in kc[41:64]]


    def get_majority(self, b1, b2, b3):
        return 1 if (b1 + b2 + b3) >= 2 else 0


    def clock(self):
        # Clocking bits (taps) for majority logic
        m1, m2, m3 = self.r1[8], self.r2[10], self.r3[10]
        maj = self.get_majority(m1, m2, m3)


        # Shift registers only if they match the majority
        if m1 == maj:
            feedback = self.r1[13] ^ self.r1[16] ^ self.r1[17] ^ self.r1[18]
            self.r1 = [feedback] + self.r1[:-1]
        if m2 == maj:
            feedback = self.r2[20] ^ self.r2[21]
            self.r2 = [feedback] + self.r2[:-1]
        if m3 == maj:
            feedback = self.r3[7] ^ self.r3[20] ^ self.r3[21] ^ self.r3[22]
            self.r3 = [feedback] + self.r3[:-1]


        # Output bit is XOR of all three last bits
        return self.r1[-1] ^ self.r2[-1] ^ self.r3[-1]


# --- PART 3: Execution ---
sres, session_key = generate_keys()
cipher = A5_1_Cipher(session_key)


print(f"Auth Result (SRES): {bin(sres)[2:].zfill(32)}")
print(f"Session Key (Kc):   {session_key}")


# Encrypting a sample message 'HI'
message = "HI"
message_bits = ''.join(format(ord(c), '08b') for c in message)
encrypted = ""


for bit in message_bits:
    keystream_bit = cipher.clock()
    encrypted += str(int(bit) ^ keystream_bit)


print(f"Message Bits:       {message_bits}")
print(f"Encrypted Bits:     {encrypted}")
